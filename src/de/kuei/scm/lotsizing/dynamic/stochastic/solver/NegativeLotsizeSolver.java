/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import org.apache.commons.math3.distribution.RealDistribution;

import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.DPBackwardRecursionPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.DPLotSizingDecision;

/**
 * @author Andi Popp
 *
 */
public class NegativeLotsizeSolver extends AbstractStochasticLotSizingDPSolver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected DPLotSizingDecision getDecision(
			DPBackwardRecursionPeriod[] periods, int setupPeriod,
			int nextSetupPeriod, double alpha)
			throws ConvolutionNotDefinedException {
		
		//Get the vector of aggregated demand (there is not ADI in this approach)
		RealDistribution[] demand = new RealDistribution[nextSetupPeriod];
		for (int i = 0; i < demand.length; i++){
			demand[i] = periods[i].period.getAggregatedDemandDistribution();
		}
		
		//TODO
		return null;
	}



	

}
