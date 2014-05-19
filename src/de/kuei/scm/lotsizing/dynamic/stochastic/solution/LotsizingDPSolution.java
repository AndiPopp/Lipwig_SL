/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;



/**
 * A solution for a Lot sizing problem based on a DP solution
 * @author Andi Popp
 *
 */
public class LotsizingDPSolution extends
		AbstractStochasticLotSizingDPSolution {

	/**
	 * The amount variable name
	 */
	String amountVariableName;
	
	/**
	 * Full parameter constructor
	 * @param periods
	 * @param amountVariableName
	 */
	public LotsizingDPSolution(DPBackwardRecursionPeriod[] periods, String amountVariableName) {
		super(periods);
		this.amountVariableName = amountVariableName;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution#getAmountVariableName()
	 */
	@Override
	public String getAmountVariableName() {
		return this.amountVariableName;
	}

	

}
