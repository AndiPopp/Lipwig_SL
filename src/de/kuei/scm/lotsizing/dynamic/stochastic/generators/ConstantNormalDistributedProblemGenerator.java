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

	/**
	 * Constructs a normal distrubeted problem
	 * @param T number of periods
	 * @param mean demand mean
	 * @param sd demand standard deviation
	 * @param setupCost constant setup costs
	 * @param inventoryHoldingCost constant inventory holding costs
	 * @param alpha alpha service level
	 * @param orderGenerator the order generator
	 * @return
	 */
	public static NormalDistributedStochasticLotsizingProblem generate(
			int T, double mean, double sd,
			double setupCost, double inventoryHoldingCost,
			float alpha, AbstractNormalLikeOrdersGenerator orderGenerator){
		
		NormalDistributedLotSizingPeriod[] periods = new NormalDistributedLotSizingPeriod[T];
		for (int i = 0; i < periods.length; i++){
			periods[i] = new NormalDistributedLotSizingPeriod(setupCost, inventoryHoldingCost, orderGenerator.getOrderDistributions(mean, sd));
		}
		return new NormalDistributedStochasticLotsizingProblem("Generated: ConstantNormalDistributedProblemGenerator  (1)", periods, alpha);
	
	}
	
	/**
	 * Simplified generator. Sets holding costs to 1 and takes the CV instead of the mean 
	 * @param T number of periods
	 * @param mean demand mean
	 * @param cv demand coefficient of variance
	 * @param setupCost constant setup costs
	 * @param alpha alpha service level
	 * @param orderGenerator the order generator
	 * @return
	 */
	public static NormalDistributedStochasticLotsizingProblem generate(
			int T, double mean, double cv,
			double setupCost, 
			float alpha, AbstractNormalLikeOrdersGenerator orderGenerator){
		
		NormalDistributedLotSizingPeriod[] periods = new NormalDistributedLotSizingPeriod[T];
		for (int i = 0; i < periods.length; i++){
			periods[i] = new NormalDistributedLotSizingPeriod(setupCost, 1, orderGenerator.getOrderDistributions(mean, cv*mean));
		}
		return new NormalDistributedStochasticLotsizingProblem("Generated: ConstantNormalDistributedProblemGenerator (2)", periods, alpha);
	
	}
}
