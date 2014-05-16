/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import org.apache.commons.math3.distribution.RealDistribution;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.distribution.NegatableDistribution;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.DPBackwardRecursionPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.DPLotSizingDecision;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.StockFunction;

/**
 * @author Andi Popp
 *
 */
public class StaticDynamicUncertaintyWithAOINLHSolver extends AbstractStochasticLotSizingDPSolver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
		double openOrderUpToLevel = openCycleDemand.inverseCumulativeProbability(alpha);
		
		//Start with the setup cost
		double decisionCost = periods[setupPeriod].period.getSetupCost();
		//Calculate and add the inventory cost
		for (int tau = 0; tau < openOrders.length; tau++){
			RealDistribution openDemandDistribution = Convoluter.convolute(openOrders, tau+1);
			NegatableDistribution realizedDemandDistribution = (NegatableDistribution) Convoluter.convolute(realisedOrders, tau+1, realisedOrders.length);
			RealDistribution effectiveDemandDistribution = Convoluter.convolute(openDemandDistribution, realizedDemandDistribution.negate());			
			StockFunction stockFunction = new StockFunction(effectiveDemandDistribution);
			decisionCost += periods[tau].period.getInventoryHoldingCost()*stockFunction.value(openOrderUpToLevel);
		}
		
		//return
		return new DPLotSizingDecision(periods[nextSetupPeriod], decisionCost, openOrderUpToLevel);
	}

	@Override
	protected String getAmoutVariableName() {
		return "Order-Up-To-Level";
	}



	

}
