/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import org.apache.commons.math3.distribution.RealDistribution;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractLotSizingPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.SimpleStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.StockFunction;

/**
 * This solver is wrong and only kept for historic purposes
 * @author Andi Popp
 *
 */
@Deprecated //Wrong!
public class StaticDynamicUncertaintySolver extends
	AbstractStochasticLotSizingFullEnumerationSolver {

	/*
	 * (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingFullEnumerationSolver#solve(de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem, boolean[])
	 */
	public SimpleStochasticLotSizingSolution solve(AbstractStochasticLotSizingProblem problem, boolean[] setupPattern) throws SolvingInitialisiationException, ConvolutionNotDefinedException{
		//Check inputs
		if (problem.getPeriods().length != setupPattern.length) throw new SolvingInitialisiationException("Setup Pattern and problem have different planning horizons in solve(AbstractStochasticLotSizingProblem problem, boolean[] setupPattern)");
		
		//Calculate the cycle demands and cycle indexes
		int N = 1;
		for (int i = 1; i < setupPattern.length;i++){
			if (setupPattern[i]) N++;
		}
		AbstractLotSizingPeriod[] periods = problem.getPeriods();
		RealDistribution[] cycleDemand = new RealDistribution[N];
		int[] setupPeriods = new int[N];
		
		///Period 0
		int pointer = 0;
		cycleDemand[pointer] = periods[0].totalDemand();
		setupPeriods[pointer] = 0;
		
		///Other periods
		for (int i = 1; i < periods.length; i++){
			if (setupPattern[i]){
				pointer++;
				setupPeriods[pointer] = i;
				cycleDemand[pointer] = periods[i].totalDemand();
			}
			else{
				cycleDemand[pointer] = Convoluter.convolute(cycleDemand[pointer], periods[i].totalDemand());
			}
		}
				
		//calculate the order-up-to levels
		double[] amountVariableValues = new double[setupPattern.length]; //solution values in period grid
		double[] orderUpToLevel = new double[N]; //solution values in cycle grid
		for (int n = 0; n < cycleDemand.length; n++){
			orderUpToLevel[n] = cycleDemand[n].inverseCumulativeProbability(problem.getServiceLevel());
			amountVariableValues[setupPeriods[n]] = orderUpToLevel[n];
		}
		
		//solve the problem via recursion
		double totalInventoryCosts = recursiveCostFunctionG(0, problem, cycleDemand, orderUpToLevel, setupPeriods);
		//add setup costs
		double totalSetupCosts = 0.0;
		for (int i = 0; i < setupPattern.length; i++){
			if (setupPattern[i]) totalSetupCosts += periods[i].getSetupCost();
		}
		
		return new SimpleStochasticLotSizingSolution(totalSetupCosts, totalInventoryCosts, "order-up-to-level", amountVariableValues, setupPattern);
	}
	
	/**
	 * Calculates the inventory costs for the effective cycle (n,m)
	 * @param problem the original lot sizing problem
	 * @param orderUpToLevel 
	 * @param rN the n-th setupPeriod
	 * @param rMplus1 the (m+1)-th setup period (the exclusive end of the effective cycle)
	 * @return the inventory cost for the effective cycle
	 * @throws ConvolutionNotDefinedException if the demand cannot be convoluted by {@link Convoluter}
	 */
	private double prob(RealDistribution[] cycleDemand, double[] orderUpToLevels, int n, int m) throws ConvolutionNotDefinedException{
		double prob = 1;
		RealDistribution demand = cycleDemand[n];
		
		//Periods n+1 to m get absorbed
		for (int nu = n+1; nu <= m; nu++){
			prob = prob * demand.cumulativeProbability(orderUpToLevels[n]-orderUpToLevels[nu]);
			demand = Convoluter.convolute(demand, cycleDemand[nu]);
		}
		
		//Period m+1 does not get absorbed
		if (m == cycleDemand.length-1) return prob; //Dummy period N+1 has prob=1
		else return prob*(1-demand.cumulativeProbability(orderUpToLevels[n]-orderUpToLevels[m+1]));
	}
	
	/**
	 * Calculates the inventory costs for the effective cycle (n,m)
	 * @param problem the original lot sizing problem
	 * @param orderUpToLevel 
	 * @param rN the n-th setupPeriod
	 * @param rMplus1 the (m+1)-th setup period (the exclusive end of the effective cycle)
	 * @return the inventory cost for the effective cycle
	 * @throws ConvolutionNotDefinedException if the demand cannot be convoluted by {@link Convoluter}
	 */
	private double inventoryCostFunctionC(AbstractStochasticLotSizingProblem problem, double orderUpToLevel, int rN, int rMplus1) throws ConvolutionNotDefinedException{
		AbstractLotSizingPeriod[] periods = problem.getPeriods();
		//initialize with first period
		RealDistribution aggregatedDemand = periods[rN].totalDemand();
		StockFunction stockFunction = new StockFunction(aggregatedDemand);
		double costs = periods[rN].getInventoryHoldingCost()*stockFunction.value(orderUpToLevel);

		//calculate other periods
		for (int k = rN+1; k < rMplus1; k++){
			aggregatedDemand = Convoluter.convolute(aggregatedDemand, periods[k].totalDemand());
			stockFunction = new StockFunction(aggregatedDemand);
			costs += periods[k].getInventoryHoldingCost()*stockFunction.value(orderUpToLevel);
		}
		
		return costs;
	}

	/**
	 * The recursive cost function G_n
	 * @param n the cycle index n 
	 * @param problem the original problem. Needed to calculate the exact inventory costs
	 * @param cycleDemand the precalculated cycle demand
	 * @param orderUpToLevels the precalculated order-up-to-levels
	 * @param setupPeriods the setup periods; precalculated from the setup pattern
	 * @return the value of the recursive cost function
	 * @throws ConvolutionNotDefinedException if the demand cannot be convoluted by {@link Convoluter}
	 */
	private double recursiveCostFunctionG(int n, AbstractStochasticLotSizingProblem problem, RealDistribution[] cycleDemand, double[] orderUpToLevels, int[] setupPeriods) throws ConvolutionNotDefinedException{
		double value = 0;
		
		for (int m = n; m < orderUpToLevels.length; m++){
			//Calculate probability of effective cycle (n,m)
			double p = prob(cycleDemand, orderUpToLevels, n, m);
			
			//Calculate value of function C(n,m)
			double c;
			if (m == orderUpToLevels.length-1){
				c = inventoryCostFunctionC(problem, orderUpToLevels[n], setupPeriods[n], problem.getPeriods().length);
			}
			else{
				c = inventoryCostFunctionC(problem, orderUpToLevels[n], setupPeriods[n], setupPeriods[m+1]);
			}
			
			//Calculate value of function G(m+1)
			double g;
			if (m == orderUpToLevels.length-1){
				g = 0;
			}
			else{
				g = recursiveCostFunctionG(m+1, problem, cycleDemand, orderUpToLevels, setupPeriods);
			}
						
			//Add everything up
			value += p * (c+g);
		
//			System.out.println("DEBUG: Eff. Cycle ("+n+"/"+m+")");
//			System.out.println("  p = "+p);
//			System.out.println("  C = "+c);
//			System.out.println("  G = "+g);
//			System.out.println("  Sum = "+value);
//			System.out.println();
		}
		
		return value;
	}
}
