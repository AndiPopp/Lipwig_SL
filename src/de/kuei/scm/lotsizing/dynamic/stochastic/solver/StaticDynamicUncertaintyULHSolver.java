/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import org.apache.commons.math3.distribution.RealDistribution;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.DPBackwardRecursionPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.DPLotSizingDecision;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.SimpleStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.StockFunction;

/**
 * @author Andi Popp
 *
 */
public class StaticDynamicUncertaintyULHSolver extends AbstractStochasticLotSizingDPSolver{

	@Override
	protected DPLotSizingDecision getDecision(
			DPBackwardRecursionPeriod[] periods, int setupPeriod,
			int nextSetupPeriod, double alpha)
			throws ConvolutionNotDefinedException {
		
		//Get the vector of aggregated demand (there is no ADI in this approach)
		RealDistribution[] demand = new RealDistribution[nextSetupPeriod-setupPeriod];
		for (int i = 0; i < demand.length; i++){
			demand[i] = periods[setupPeriod+i].period.totalDemand();
		}
		
		//Get the total production up to the beginning of the next setup period (i.e. exclusively!)
		RealDistribution cycleDemand = Convoluter.convolute(demand);
		double orderUpToLevel = cycleDemand.inverseCumulativeProbability(alpha);
		
		//Start with the setup cost
		double decisionCost = periods[setupPeriod].period.getSetupCost();
		//Calculate and add the inventory cost
		for (int tau = 0; tau < demand.length; tau++){
			RealDistribution totalDemandDistribution = Convoluter.convolute(demand, tau+1);
			StockFunction stockFunction = new StockFunction(totalDemandDistribution);
			decisionCost += periods[tau].period.getInventoryHoldingCost()*stockFunction.value(orderUpToLevel);
		}
				
		//return
		return new DPLotSizingDecision(periods[nextSetupPeriod], decisionCost, orderUpToLevel);
	}

	@Override
	protected String getAmoutVariableName() {
		return "Order-Up-To-Level";
	}

	@Override
	//Quick and dirty implementation, needs some hard revision
	public AbstractStochasticLotSizingSolution solve(
			AbstractStochasticLotSizingProblem problem, boolean[] setupPattern)
			throws SolvingInitialisiationException,
			ConvolutionNotDefinedException {
		
		//Check inputs
		if (problem.getPeriods().length != setupPattern.length) throw new SolvingInitialisiationException("Problem and setup pattern have different planning horizons.");
		
		//Get the vector of aggregated demand (there is no ADI in this approach)
		RealDistribution[] demand = new RealDistribution[problem.getPeriods().length];
		for (int i = 0; i < demand.length; i++){
			demand[i] = problem.getPeriods()[i].totalDemand();
		}
				
		int setupPeriod = -1;
		RealDistribution cycleDemand = null;
		double inventoryCost = 0.0;
		double setupCost = 0.0;
		double[] orderUpToLevel = new double[setupPattern.length];
		
		for (int t = 0; t < demand.length;t++){
			if (setupPattern[t]){
				setupCost += problem.getPeriods()[t].getSetupCost();
				cycleDemand = problem.getPeriods()[t].totalDemand();
				int nextSetupPeriod = t+1;
				
				while(nextSetupPeriod < demand.length && !setupPattern[nextSetupPeriod]){
					cycleDemand = Convoluter.convolute(cycleDemand, cycleDemand = problem.getPeriods()[nextSetupPeriod].totalDemand());
					nextSetupPeriod++;
				}
				
				orderUpToLevel[t] = cycleDemand.inverseCumulativeProbability(problem.getServiceLevel());
				setupPeriod = t;
				
				cycleDemand = problem.getPeriods()[t].totalDemand();
				StockFunction stockFunction = new StockFunction(cycleDemand);
				inventoryCost += problem.getPeriods()[t].getInventoryHoldingCost()*stockFunction.value(orderUpToLevel[setupPeriod]);
			}
			else{
				cycleDemand = Convoluter.convolute(cycleDemand,problem.getPeriods()[t].totalDemand());
				StockFunction stockFunction = new StockFunction(cycleDemand);
				inventoryCost += problem.getPeriods()[t].getInventoryHoldingCost()*stockFunction.value(orderUpToLevel[setupPeriod]);
			}
		}
		
		return new SimpleStochasticLotSizingSolution(setupCost, inventoryCost, "Order-up-to-level", orderUpToLevel, setupPattern);
		
	}



	

}
