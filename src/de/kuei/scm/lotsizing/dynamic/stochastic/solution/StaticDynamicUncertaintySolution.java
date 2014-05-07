/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;

/**
 * This class represents an exact solution for a stochastic dynamic lot sizing algorithm
 * calculated e.g. by the algorithm of Özen et. al [2012], sec. 6
 * @author Andi Popp
 *
 */
public class StaticDynamicUncertaintySolution extends
		AbstractStochasticLotSizingSolution {

	/**
	 * The solved periods
	 */
	SimpleSolvedPeriod[] periods;

	/**
	 * A variable to save the objective value to
	 */
	private Double objectiveValue;
	
	/**
	 * Creates a new solution with the given solved periods
	 * @param periods
	 */
	public StaticDynamicUncertaintySolution(SimpleSolvedPeriod[] periods) {
		this.periods = periods;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution#getObjectiveValue()
	 */
	@Override
	public double getObjectiveValue() {
		if (objectiveValue != null) return objectiveValue;
		else calculateObjectiveValue();
	
		return objectiveValue;
	}
	
	/**
	 * Creates the objective value and stores it in the private field {@link #objectiveValue}. The
	 * value can be read via the method {@link #getObjectiveValue()}.
	 */
	public void calculateObjectiveValue(){
		//TODO
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution#getAmountVariableValues()
	 */
	@Override
	public double[] getAmountVariableValues() {
		double[] amountVariables = new double[periods.length];
		for (int i = 0; i < amountVariables.length; i++){
			amountVariables[i] = periods[i].amount;
		}
		return amountVariables;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution#getAmountVariableName()
	 */
	@Override
	public String getAmountVariableName() {
		return "order-up-to-level";
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution#getSetupPattern()
	 */
	@Override
	public boolean[] getSetupPattern() {
		boolean[] setuPattern = new boolean[periods.length];
		for (int i = 0; i < setuPattern.length; i++){
			setuPattern[i] = periods[i].setup;
		}
		return setuPattern;
	}

}
