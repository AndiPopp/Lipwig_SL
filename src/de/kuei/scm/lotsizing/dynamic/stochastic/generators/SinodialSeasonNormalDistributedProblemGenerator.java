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
public class SinodialSeasonNormalDistributedProblemGenerator extends
		AbstractLotSizingProblemGenerator {


	/**
	 * Creates a problem with a sinodial season demand pattern
	 * @param quarterPeriodLength length of one sin quarter period in demand periods
	 * @param numberOfQuarterPeriods number of sin quarter periods
	 * @param baseLevelMean the base level mean, i.e. the mean of the demand where sin is 0
	 * @param amplitudeMean the amplitude mean. Added to the base level mean where sin is 1
	 * @param cv demand coefficient of variance
	 * @param setupCost constant setup costs
	 * @param inventoryHoldingCost constant inventory holding costs
	 * @param alpha alpha service level
	 * @param orderGenerator the order generator
	 * @return
	 */
	public static NormalDistributedStochasticLotsizingProblem generate(
			int quarterPeriodLength, int numberOfQuarterPeriods, double baseLevelMean, double amplitudeMean, double cv,
			double setupCost, double inventoryHoldingCost,
			float alpha, AbstractNormalLikeOrdersGenerator orderGenerator){
		
		//calculate sin step
		double sinStep = (Math.PI/2)/quarterPeriodLength;
		
		NormalDistributedLotSizingPeriod[] periods = new NormalDistributedLotSizingPeriod[quarterPeriodLength*numberOfQuarterPeriods];
		for (int i = 0; i < periods.length; i++){
			double mean = baseLevelMean+(Math.sin(i*sinStep)*amplitudeMean);
			periods[i] = new NormalDistributedLotSizingPeriod(setupCost, inventoryHoldingCost, orderGenerator.getOrderDistributions(mean, mean*cv));
		}
		return new NormalDistributedStochasticLotsizingProblem("Generated: SinodialSeasonNormalDistributedProblemGenerator (1)", periods, alpha);

	}
	

}
