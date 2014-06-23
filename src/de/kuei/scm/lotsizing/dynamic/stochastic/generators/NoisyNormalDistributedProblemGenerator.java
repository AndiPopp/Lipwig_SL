/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.generators;

import org.apache.commons.math3.distribution.AbstractRealDistribution;

import de.kuei.scm.lotsizing.dynamic.stochastic.NormalDistributedLotSizingPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.NormalDistributedStochasticLotsizingProblem;

/**
 * This generator constructs a problem with a trended normal distrubeted demand
 * @author Andi Popp
 *
 */
public class NoisyNormalDistributedProblemGenerator extends
		AbstractLotSizingProblemGenerator {

	
	
	/**
	 * Creates a problem with a noisy demand pattern
	 * @param T planning horizon
	 * @param meanDistribution The distribution of the demand mean
	 * @param cv the demand coefficient of variance
	 * @param seed The seed to sample the mean distributions
	 * @param setupCost constant setup costs
	 * @param inventoryHoldingCost constant inventory holding costs
	 * @param alpha alpha service level
	 * @param orderGenerator the order generator
	 * @return
	 */
	public static NormalDistributedStochasticLotsizingProblem generate(
			int T, AbstractRealDistribution meanDistribution, double cv, long seed,
			double setupCost, double inventoryHoldingCost,
			float alpha, AbstractNormalLikeOrdersGenerator orderGenerator){
		
		meanDistribution.reseedRandomGenerator(seed);
		
		NormalDistributedLotSizingPeriod[] periods = new NormalDistributedLotSizingPeriod[T];
		for (int i = 0; i < periods.length; i++){
			double mean = meanDistribution.sample();
			periods[i] = new NormalDistributedLotSizingPeriod(setupCost, inventoryHoldingCost, orderGenerator.getOrderDistributions(mean, mean*cv));
		}
		return new NormalDistributedStochasticLotsizingProblem("Generated: SinodialSeasonNormalDistributedProblemGenerator (1)", periods, alpha);
	
	}

}
