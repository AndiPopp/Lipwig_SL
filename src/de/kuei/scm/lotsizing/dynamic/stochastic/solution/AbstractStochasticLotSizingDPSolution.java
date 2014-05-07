/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;



/**
 * @author Andi Popp
 *
 */
public abstract class AbstractStochasticLotSizingDPSolution extends
		AbstractStochasticLotSizingSolution {

	/**
	 * The array of the DP solver ready wrapped periods
	 */
	public DPBackwardRecursionPeriod[] periods;
	
	/**
	 * Full parameter constructor
	 * @param periods
	 */
	public AbstractStochasticLotSizingDPSolution(DPBackwardRecursionPeriod[] periods) {
		this.periods = periods;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution#getGoalValue()
	 */
	@Override
	public double getObjectiveValue() {
		return periods[0].partialOptimalValue;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution#getAmountVariableValues()
	 */
	@Override
	public double[] getAmountVariableValues() {
		double[] amountVariableValues = new double[periods.length-1];
		for (int i = 0; i < amountVariableValues.length; i++){
			amountVariableValues[i] = periods[i].optimalDecision.amount * getNumericalSetupPattern()[i];
		}
		return amountVariableValues;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution#getSetupPattern()
	 */
	@Override
	public boolean[] getSetupPattern() {
		boolean[] setupPattern = new boolean[periods.length-1];
		
		//The first period is the first setup period
		DPBackwardRecursionPeriod currentPeriod = periods[0];
		
		//Climb along the optimal decisions and fill the setup pattern
		for (int i = 0; i < setupPattern.length; i++){
			if (currentPeriod.equals(periods[i])){
				setupPattern[i] = true;
				currentPeriod = currentPeriod.optimalDecision.nextSetupPeriod;
			}
			else{
				setupPattern[i] = false;
			}
		}		
		
		//return the setup pattern
		return setupPattern;
	}

}
