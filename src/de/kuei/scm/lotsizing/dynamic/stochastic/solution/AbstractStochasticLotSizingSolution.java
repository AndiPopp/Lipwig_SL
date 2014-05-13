/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;

/**
 * This is a container class, which saves a possible solution for
 * a stochastic dynamic lot sizing problem.
 * @author Andi Popp
 *
 */
public abstract class AbstractStochasticLotSizingSolution {

	/**
	 * Gets the value of the goal function as calculated by the solver for this solution
	 * @return the value of the objective function as calculated by the solver for this solution
	 */
	public abstract double getObjectiveValue();
	
	/**
	 * Gets the value of the amount variables for each period.
	 * @return An array representing the value of the value of the amount variables for each period.
	 * The array index corresponds to the period index.
	 */
	public abstract double[] getAmountVariableValues();
	
	/**
	 * The amount variable name declares how to actually call the
	 * amount variables (e.g. lot size, order-up-to-level)
	 * @return the amount variable name
	 */
	public abstract String getAmountVariableName();
	
	/**
	 * Gives the logical setup pattern for the problem.
	 * @return An array representing the logical setup pattern. If the value for an
	 * index t is true, this means period t is a setup period.
	 */
	public abstract boolean[] getSetupPattern();
	
	/**
	 * Translates the logical setup pattern of {@link #getSetupPattern()} into a
	 * numerical (0,1) setup pattern.
	 * @return the numerical setup pattern
	 */
	public byte[] getNumericalSetupPattern(){
		boolean[] setupPattern = getSetupPattern();
		byte[] numericalSetupPattern = new byte[setupPattern.length];
		for (int i = 0; i < setupPattern.length; i++){
			if (setupPattern[i]) numericalSetupPattern[i] = 1;
			else numericalSetupPattern[i] = 0;
		}
		return numericalSetupPattern;
	}
	
	public void print(){
		boolean[] setupPattern = getSetupPattern();
		double[] amountVariableValues = getAmountVariableValues();
		System.out.println("Period - "+getAmountVariableName());
		System.out.println("---");
		for (int i = 0; i < setupPattern.length; i++){
			if (setupPattern[i]) System.out.println(i+" - "+amountVariableValues[i]);
		}
	}
}
