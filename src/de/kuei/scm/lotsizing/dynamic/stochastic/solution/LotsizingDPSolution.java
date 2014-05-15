/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;



/**
 * @author Andi Popp
 *
 */
public class LotsizingDPSolution extends
		AbstractStochasticLotSizingDPSolution {

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
