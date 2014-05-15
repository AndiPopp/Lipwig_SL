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
 * This solver calculates the solution for a stochastic dynamic lot sizing solution with
 * static uncertainty strategy as proposed by Bookbinder & Tan [1988] with the modified
 * objective function proposed by Popp [2014].
 * @author Andi Popp
 *
 */
public class StaticUncertaintySolver extends AbstractStochasticLotSizingDPSolver {

	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = -2262997917688083174L;

	
	/*
	 * (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingDPSolver#getDecision(de.kuei.scm.lotsizing.dynamic.stochastic.solver.DPBackwardRecursionPeriod[], int, int, double)
	 */
	@Override
	protected DPLotSizingDecision getDecision(DPBackwardRecursionPeriod[] periods, int setupPeriod, int nextSetupPeriod, double alpha) throws ConvolutionNotDefinedException{
		//Get the vector of aggregated demand (there is no ADI in static uncertainty)
		RealDistribution[] demand = new RealDistribution[nextSetupPeriod];
		for (int i = 0; i < demand.length; i++){
			demand[i] = periods[i].period.getAggregatedDemandDistribution();
		}
		
		//Get the total production up to the beginning of the next setup period (i.e. exclusively!)
		RealDistribution totalDemandUpToNextSetupPeriod = Convoluter.convolute(demand);
		double totalProductionUpToNextSetupPeriod = totalDemandUpToNextSetupPeriod.inverseCumulativeProbability(alpha);
		
		//Start with the setup cost
		double decisionCost = periods[setupPeriod].period.getSetupCost();
		//Calculate and add the inventory cost
		for (int tau = setupPeriod; tau < nextSetupPeriod; tau++){
			RealDistribution totalDemandDistribution = Convoluter.convolute(demand, tau+1);
			StockFunction stockFunction = new StockFunction(totalDemandDistribution);
			decisionCost += periods[tau].period.getInventoryHoldingCost()*stockFunction.value(totalProductionUpToNextSetupPeriod);
		}

		//Calculate the production before the setup period to get the lot size
		double totalProductionUpToSetupPeriod = 0.0;
		if (setupPeriod > 0){
			RealDistribution totalDemandUpToSetupPeriod = Convoluter.convolute(demand, setupPeriod);
			totalProductionUpToSetupPeriod = totalDemandUpToSetupPeriod.inverseCumulativeProbability(alpha);
		}
		
		
		//return
		return new DPLotSizingDecision(periods[nextSetupPeriod], decisionCost, totalProductionUpToNextSetupPeriod-totalProductionUpToSetupPeriod);
	}


	@Override
	protected String getAmoutVariableName() {
		return "Lot size";
	}

}
