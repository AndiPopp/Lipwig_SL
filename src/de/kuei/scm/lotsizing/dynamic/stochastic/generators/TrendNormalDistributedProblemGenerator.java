/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.generators;

import de.kuei.scm.lotsizing.dynamic.stochastic.NormalDistributedLotSizingPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.NormalDistributedStochasticLotsizingProblem;

/**
 * This generator constructs a problem with a trended normal distrubeted demand
 * @author Andi Popp
 *
 */
public class TrendNormalDistributedProblemGenerator extends
		AbstractLotSizingProblemGenerator {

	/**
	 * Constructs a normal distributed problem. The first mean is the demand mean in the first period, the 
	 * last mean is the mean in the last period. All means between will be linearly adjusted.
	 * @param T number of periods
	 * @param firstMean demand mean
	 * @param lastMean demand mean
	 * @param cv demand coefficient of variance
	 * @param setupCost constant setup costs
	 * @param inventoryHoldingCost constant inventory holding costs
	 * @param alpha alpha service level
	 * @param orderGenerator the order generator
	 * @return
	 */
	public static NormalDistributedStochasticLotsizingProblem generate(
			int T, double firstMean, double lastMean, double cv,
			double setupCost, double inventoryHoldingCost,
			float alpha, AbstractNormalLikeOrdersGenerator orderGenerator){
		
		//Calculate the step for the mean
		double step = (lastMean-firstMean)/(T-1);
		
		NormalDistributedLotSizingPeriod[] periods = new NormalDistributedLotSizingPeriod[T];
		for (int i = 0; i < periods.length; i++){
			double mean = firstMean+i*step;
			periods[i] = new NormalDistributedLotSizingPeriod(setupCost, inventoryHoldingCost, orderGenerator.getOrderDistributions(mean, mean*cv));
		}
		return new NormalDistributedStochasticLotsizingProblem("Generated: TrendNormalDistributedProblemGenerator (1)", periods, alpha);
	
	}
	

}
