/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic;

import java.util.Vector;

/**
 * This class represents a single dynamic stochastic lot sizing problem with normal distributed
 * orders and therefore normal distributed aggregated demand.
 * @author Andi Popp
 *
 */
public class NormalDistributedStochasticDynamicLotsizingProblem {
	
	/**
	 * These are the lot sizing periods. They contain the demand distribution information,
	 * the setup cost parameter and the inventory holding cost parameter. See 
	 * {@link NormalDistributedLotSizingPeriod} for more details.
	 */
	NormalDistributedLotSizingPeriod[] periods;

	/**
	 * The target alpha service level for this problem
	 */
	float alpha;
	
	/**
	 * The collection of all solution calculated for this problem
	 * so far
	 */
	Vector<AbstractStochasticDynamicLotSizingSolution> solutions;
	
}
