/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;


/**
 * This class implements a DP decision of how many periods have to be covered by one
 * production lot. It points at the next setup period and gives the cost for the 
 * decision
 * @author Andi Popp
 *
 */
public class DPLotSizingDecision {

	/**
	 * The next setup period with this decision. Null pointer implicates the end of 
	 * the planing horizon
	 */
	protected DPBackwardRecursionPeriod nextSetupPeriod;
	
	/**
	 * The cost for this decision
	 */
	protected double cost;

	/**
	 * The amount value, e.g. the lot size or the order up to level
	 */
	protected double amount;
	
	/**
	 * Full parameter constructor
	 * @param nextSetupPeriod
	 * @param cost
	 * @param amount
	 */
	public DPLotSizingDecision(DPBackwardRecursionPeriod nextSetupPeriod,
			double cost, double amount) {
		this.nextSetupPeriod = nextSetupPeriod;
		this.cost = cost;
		this.amount = amount;
	}
	
	
	
}
