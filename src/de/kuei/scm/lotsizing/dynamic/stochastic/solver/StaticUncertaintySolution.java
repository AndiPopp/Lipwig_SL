/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

/**
 * @author Andi Popp
 *
 */
public class StaticUncertaintySolution extends
		AbstractStochasticLotSizingSolution {

	/**
	 * The array of the DP solver ready wrapped periods
	 */
	public DPBackwardRecursionPeriod[] periods;
	
	/**
	 * Full parameter constructor
	 * @param periods
	 */
	public StaticUncertaintySolution(DPBackwardRecursionPeriod[] periods) {
		this.periods = periods;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution#getGoalValue()
	 */
	@Override
	public double getGoalValue() {
		return periods[0].partialOptimalValue;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution#getAmountVariableValues()
	 */
	@Override
	public double[] getAmountVariableValues() {
		double[] amountVariableValues = new double[periods.length];
		for (int i = 0; i < periods.length; i++){
			amountVariableValues[0] = periods[0].optimalDecision.amount;
		}
		return amountVariableValues;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution#getAmountVariableName()
	 */
	@Override
	public String getAmountVariableName() {
		return "lot size";
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution#getSetupPattern()
	 */
	@Override
	public boolean[] getSetupPattern() {
		boolean[] setupPattern = new boolean[periods.length];
		
		//The first period is the first setup period
		DPBackwardRecursionPeriod currentPeriod = periods[0];
		
		//Climb along the optimal decisions and fill the setup pattern
		for (int i = 0; i < periods.length; i++){
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
