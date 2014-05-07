/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;



/**
 * @author Andi Popp
 *
 */
public class StaticUncertaintySolution extends
		AbstractStochasticLotSizingDPSolution {

	/**
	 * Full parameter constructor
	 * @param periods
	 */
	public StaticUncertaintySolution(DPBackwardRecursionPeriod[] periods) {
		super(periods);
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution#getAmountVariableName()
	 */
	@Override
	public String getAmountVariableName() {
		return "lot size";
	}

	

}
