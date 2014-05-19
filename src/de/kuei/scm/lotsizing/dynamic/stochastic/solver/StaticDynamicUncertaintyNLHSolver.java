/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import org.apache.commons.math3.distribution.RealDistribution;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.DPBackwardRecursionPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.DPLotSizingDecision;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.StockFunction;

/**
 * @author Andi Popp
 *
 */
public class StaticDynamicUncertaintyNLHSolver extends AbstractStochasticLotSizingDPSolver{

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



	

}
