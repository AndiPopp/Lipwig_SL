/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import org.apache.commons.math3.distribution.RealDistribution;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.distribution.NegatableDistribution;
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
public class StaticDynamicUncertaintyWithADIULHSolver extends AbstractStochasticLotSizingDPSolver{



	@Override
	protected DPLotSizingDecision getDecision(
			DPBackwardRecursionPeriod[] periods, int setupPeriod,
			int nextSetupPeriod, double alpha)
			throws ConvolutionNotDefinedException {
		
		//Get the vector of open and realized demand
		RealDistribution[] openOrders = new RealDistribution[nextSetupPeriod-setupPeriod];
		RealDistribution[] realisedOrders = new RealDistribution[nextSetupPeriod-setupPeriod];
		for (int i = 0; i < openOrders.length; i++){
			openOrders[i] = periods[setupPeriod+i].period.openDemand(setupPeriod+i, setupPeriod);
			realisedOrders[i] = periods[setupPeriod+i].period.realisedDemand(setupPeriod+i, setupPeriod);
		}
		
		//Get the open order up to level
		RealDistribution openCycleDemand = Convoluter.convolute(openOrders);
		double openOrderUpToLevelForOpenDemand = openCycleDemand.inverseCumulativeProbability(alpha);
		
		//Start with the setup cost
		double decisionCost = periods[setupPeriod].period.getSetupCost();
		//Calculate and add the inventory cost
		for (int tau = 0; tau < openOrders.length; tau++){
			RealDistribution openDemandDistribution = Convoluter.convolute(openOrders, tau+1);
			NegatableDistribution realizedDemandDistribution = (NegatableDistribution) Convoluter.convolute(realisedOrders, tau+1, realisedOrders.length);
			RealDistribution effectiveDemandDistribution = Convoluter.convolute(openDemandDistribution, realizedDemandDistribution.negate());			
			StockFunction stockFunction = new StockFunction(effectiveDemandDistribution);
			decisionCost += periods[tau].period.getInventoryHoldingCost()*stockFunction.value(openOrderUpToLevelForOpenDemand);
		}
		
		//return
		return new DPLotSizingDecision(periods[nextSetupPeriod], decisionCost, openOrderUpToLevelForOpenDemand);
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
		RealDistribution openOrders = null;
		NegatableDistribution realizedOrders = null;
		NegatableDistribution[] realizedOrderArray = null;		
		
		int setupPeriod = -1;
		double inventoryCost = 0.0;
		double setupCost = 0.0;
		double[] orderUpToLevelForOpenDemand = new double[setupPattern.length];
		
		for (int t = 0; t < setupPattern.length;t++){
			if (setupPattern[t]){
				setupPeriod = t;
				setupCost += problem.getPeriods()[t].getSetupCost();
				int nextSetupPeriod = t+1;
				
				while(nextSetupPeriod < setupPattern.length && !setupPattern[nextSetupPeriod]){
					nextSetupPeriod++;
				}
				
				openOrders = problem.getPeriods()[t].openDemand(t, setupPeriod);
				realizedOrderArray = new NegatableDistribution[nextSetupPeriod-setupPeriod];
				realizedOrderArray[0] = (NegatableDistribution) problem.getPeriods()[t].realisedDemand(t, setupPeriod);
				for (int tau = t+1; tau < nextSetupPeriod; tau++){
					 openOrders = Convoluter.convolute(openOrders, problem.getPeriods()[tau].openDemand(tau, setupPeriod));
					 realizedOrderArray[tau-t] = (NegatableDistribution) problem.getPeriods()[tau].realisedDemand(tau, setupPeriod);
				}
				
				orderUpToLevelForOpenDemand[t] = openOrders.inverseCumulativeProbability(problem.getServiceLevel());
				
				
				openOrders = problem.getPeriods()[t].openDemand(t, setupPeriod);
				realizedOrders = (NegatableDistribution) Convoluter.convolute(realizedOrderArray, 1, realizedOrderArray.length);
				RealDistribution effectiveDemandDistribution = Convoluter.convolute(openOrders, realizedOrders.negate());
				StockFunction stockFunction = new StockFunction(effectiveDemandDistribution);
				inventoryCost += problem.getPeriods()[t].getInventoryHoldingCost()*stockFunction.value(orderUpToLevelForOpenDemand[setupPeriod]);
			}
			else{
				openOrders = Convoluter.convolute(openOrders,problem.getPeriods()[t].openDemand(t, setupPeriod));
				realizedOrders = (NegatableDistribution) (NegatableDistribution) Convoluter.convolute(realizedOrderArray, t-setupPeriod+1, realizedOrderArray.length);
				RealDistribution effectiveDemandDistribution = Convoluter.convolute(openOrders, realizedOrders.negate());
				StockFunction stockFunction = new StockFunction(effectiveDemandDistribution);
				inventoryCost += problem.getPeriods()[t].getInventoryHoldingCost()*stockFunction.value(orderUpToLevelForOpenDemand[setupPeriod]);
			}
		}
		
		return new SimpleStochasticLotSizingSolution(setupCost, inventoryCost, "Order-up-to-level for open demand", orderUpToLevelForOpenDemand, setupPattern);
		
	}

	

	@Override
	protected String getAmoutVariableName() {
		return "Order-Up-To-Level";
	}



	

}
