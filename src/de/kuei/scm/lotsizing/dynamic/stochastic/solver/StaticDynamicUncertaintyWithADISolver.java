/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import org.apache.commons.math3.distribution.RealDistribution;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.distribution.NegatableDistribution;
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
public class StaticDynamicUncertaintyWithADISolver extends
		AbstractStochasticLotSizingFullEnumerationSolver {


	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingFullEnumerationSolver#solve(de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem, boolean[])
	 */
	@Override
	public SimpleStochasticLotSizingSolution solve(
			AbstractStochasticLotSizingProblem problem, boolean[] setupPattern)
			throws SolvingInitialisiationException,
			ConvolutionNotDefinedException {
		
		//Calculate the cycle demands and cycle indexes
		int N = 1;
		for (int i = 1; i < setupPattern.length;i++){
			if (setupPattern[i]) N++;
		}
		AbstractLotSizingPeriod[] periods = problem.getPeriods();
		RealDistribution[] totalCycleDemand = new RealDistribution[N];
		RealDistribution[] openCycleDemand = new RealDistribution[N];
		RealDistribution[] realisedCycleDemand = new RealDistribution[N];
		int[] setupPeriods = new int[N];
		
		///Period 0
		int pointer = 0;
		setupPeriods[pointer] = 0;
		totalCycleDemand[pointer] = periods[0].totalDemand();
		openCycleDemand[pointer] = periods[0].openDemand(0, setupPeriods[pointer]);
		realisedCycleDemand[pointer] = periods[0].realisedDemand(0, setupPeriods[pointer]);
		
		
		///Other periods
		for (int t = 1; t < periods.length; t++){
			if (setupPattern[t]){
				pointer++;
				setupPeriods[pointer] = t;
				totalCycleDemand[pointer] = periods[t].totalDemand();
				openCycleDemand[pointer] = periods[t].openDemand(t, setupPeriods[pointer]);
				realisedCycleDemand[pointer] = periods[t].realisedDemand(t, setupPeriods[pointer]);
			}
			else{
				totalCycleDemand[pointer] = Convoluter.convolute(totalCycleDemand[pointer], periods[t].totalDemand());
				openCycleDemand[pointer] = Convoluter.convolute(openCycleDemand[pointer], periods[t].openDemand(t, setupPeriods[pointer]));
				realisedCycleDemand[pointer] = Convoluter.convolute(realisedCycleDemand[pointer], periods[t].realisedDemand(t, setupPeriods[pointer]));
			}
		}
		
		//calculate the order-up-to levels
		double[] amountVariableValues = new double[setupPattern.length]; //solution values in period grid
		double[] orderUpToLevelForOpenDemand = new double[N]; //solution values in cycle grid
		for (int n = 0; n < openCycleDemand.length; n++){
			orderUpToLevelForOpenDemand[n] = openCycleDemand[n].inverseCumulativeProbability(problem.getServiceLevel());
			amountVariableValues[setupPeriods[n]] = orderUpToLevelForOpenDemand[n];
		}
		
		//solve the problem via recursion
		double totalInventoryCosts = recursiveCostFunctionG(0, problem, totalCycleDemand, openCycleDemand, realisedCycleDemand, orderUpToLevelForOpenDemand, setupPeriods);
		//add setup costs
		double totalSetupCosts = 0.0;
		for (int i = 0; i < setupPattern.length; i++){
			if (setupPattern[i]) totalInventoryCosts += periods[i].getSetupCost();
		}
		
		// Auto-generated method stub
		return new SimpleStochasticLotSizingSolution(totalSetupCosts,totalInventoryCosts, "order-up-to-level for open demand", amountVariableValues, setupPattern);
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
	private double prob(RealDistribution[] totalCycleDemand, RealDistribution[] openCycleDemand, RealDistribution[] realisedCycleDemand, double[] orderUpToLevelsForOpenDemand, int n, int m) throws ConvolutionNotDefinedException{
		double prob = 1;
		if (n == totalCycleDemand.length-1) return prob; //Dummy period N+1 has prob=1
		
		RealDistribution demand = Convoluter.convolute(openCycleDemand[n],realisedCycleDemand[n+1]);
		
		//Periods n+1 to m get absorbed
		for (int nu = n+1; nu <= m; nu++){
			prob = prob * demand.cumulativeProbability(orderUpToLevelsForOpenDemand[n]-orderUpToLevelsForOpenDemand[nu]);
			demand = Convoluter.convolute(demand, openCycleDemand[nu]);
			if (nu != totalCycleDemand.length-1) demand = Convoluter.convolute(demand, realisedCycleDemand[nu+1]);
		}
		
		//Period m+1 does not get absorbed
		if (m == totalCycleDemand.length-1) return prob; //Dummy period N+1 has prob=1
		else return prob*(1-demand.cumulativeProbability(orderUpToLevelsForOpenDemand[n]-orderUpToLevelsForOpenDemand[m+1]));
	}
	
	/**
	 * The recursive cost function G_n
	 * @param n the cycle index n 
	 * @param problem the original problem. Needed to calculate the exact inventory costs
	 * @param totalCycleDemand the array of precalculated cycle demand
	 * @param openCycleDemand the array of precalculated open cycle demand at the respective setup period
	 * @param realisedCycleDemand the array of precalculated realised cylce demand ate the respective setup period
	 * @param orderUpToLevelsForOpenDemand the precalculated order-up-to-levels
	 * @param setupPeriods the setup periods; precalculated from the setup pattern
	 * @return the value of the recursive cost function
	 * @throws ConvolutionNotDefinedException if the demand cannot be convoluted by {@link Convoluter}
	 */
	private double recursiveCostFunctionG(int n, AbstractStochasticLotSizingProblem problem, RealDistribution[] totalCycleDemand, RealDistribution[] openCycleDemand, RealDistribution[] realisedCycleDemand, double[] orderUpToLevelsForOpenDemand, int[] setupPeriods) throws ConvolutionNotDefinedException{
		double value = 0;
		
		for (int m = n; m < orderUpToLevelsForOpenDemand.length; m++){
			
			//Calculate probability of effective cycle (n,m)
			double p = prob(totalCycleDemand, openCycleDemand, realisedCycleDemand, orderUpToLevelsForOpenDemand, n, m);
//			System.out.println("DEBUG. p("+n+","+m+") = "+p);
			
			//Calculate value of function C(n,m)
			double c;
			if (n == orderUpToLevelsForOpenDemand.length-1){
				c = inventoryCostFunctionC(problem, orderUpToLevelsForOpenDemand[n], setupPeriods[n], problem.getPeriods().length, problem.getPeriods().length);
			}			
			else if (m == orderUpToLevelsForOpenDemand.length-1){
				c = inventoryCostFunctionC(problem, orderUpToLevelsForOpenDemand[n], setupPeriods[n], setupPeriods[n+1], problem.getPeriods().length);
			}
			else{
				c = inventoryCostFunctionC(problem, orderUpToLevelsForOpenDemand[n], setupPeriods[n], setupPeriods[n+1], setupPeriods[m+1]);
			}
			
			//Calculate value of function G(m+1)
			double g;
			if (m == orderUpToLevelsForOpenDemand.length-1){
				g = 0;
			}
			else{
				g = recursiveCostFunctionG(m+1, problem, totalCycleDemand, openCycleDemand, realisedCycleDemand, orderUpToLevelsForOpenDemand, setupPeriods);
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
	
	
	
	/**
	 * Calculates the inventory costs for the effective cycle (n,m)
	 * @param problem the original lot sizing problem
	 * @param orderUpToLevelForOpenDemand 
	 * @param rN the n-th setupPeriod
	 * @param rMplus1 the (m+1)-th setup period (the exclusive end of the effective cycle)
	 * @return the inventory cost for the effective cycle
	 * @throws ConvolutionNotDefinedException if the demand cannot be convoluted by {@link Convoluter}
	 */
	private double inventoryCostFunctionC(AbstractStochasticLotSizingProblem problem, double orderUpToLevelForOpenDemand, int rN, int rNplus1, int rMplus1) throws ConvolutionNotDefinedException{
		
		AbstractLotSizingPeriod[] periods = problem.getPeriods();
		//initialize with first period
		double costs = 0.0;
		
		for (int tau = rN; tau < rMplus1;tau++){
			RealDistribution demand = periods[rN].openDemand(rN, rN);
			for (int i = rN+1; i <= tau; i++){
				demand = Convoluter.convolute(demand, periods[i].openDemand(i, rN));
			}
			for (int i = tau+1; i < rNplus1; i++){
				demand = Convoluter.convolute(demand, ((NegatableDistribution)periods[i].realisedDemand(i, rN)).negate());
			}
			for (int i = rNplus1; rMplus1 < tau; i++){
				demand = Convoluter.convolute(demand, periods[i].totalDemand());
			}
			StockFunction stockFunction = new StockFunction(demand);;
			costs += periods[tau].getInventoryHoldingCost()*stockFunction.value(orderUpToLevelForOpenDemand);
		}
		
		return costs;
	}

}
