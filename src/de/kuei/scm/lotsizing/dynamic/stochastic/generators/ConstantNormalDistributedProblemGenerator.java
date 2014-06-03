/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.generators;

import de.kuei.scm.lotsizing.dynamic.stochastic.NormalDistributedLotSizingPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.NormalDistributedStochasticLotsizingProblem;

/**
 * This generator constructs a problem with a constant normal distrubeted demand
 * @author Andi Popp
 *
 */
public class ConstantNormalDistributedProblemGenerator extends
		AbstractLotSizingProblemGenerator {

	
	public NormalDistributedStochasticLotsizingProblem generate(
			int T, double mean, double sd,
			double setupCost, double inventoryHoldingCost,
			float alpha, AbstractNormalLikeDemandGenerator demandGenerator){
		
		NormalDistributedLotSizingPeriod[] periods = new NormalDistributedLotSizingPeriod[T];
		for (int i = 0; i < periods.length; i++){
			periods[i] = new NormalDistributedLotSizingPeriod(setupCost, inventoryHoldingCost, demandGenerator.getOrderDistributions(mean, sd));
		}
		return new NormalDistributedStochasticLotsizingProblem("Generated: ConstantNormalDistributedProblemGenerator", periods, alpha);
	
	}
}
