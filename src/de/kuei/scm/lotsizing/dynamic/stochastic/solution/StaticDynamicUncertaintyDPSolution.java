package de.kuei.scm.lotsizing.dynamic.stochastic.solution;



public class StaticDynamicUncertaintyDPSolution extends AbstractStochasticLotSizingDPSolution{

	

	/**
	 * Full parameter constructor
	 * @param periods
	 */
	public StaticDynamicUncertaintyDPSolution(DPBackwardRecursionPeriod[] periods) {
		super(periods);
	}

	@Override
	public String getAmountVariableName() {
		return "order-up-to-level";
	}

}
