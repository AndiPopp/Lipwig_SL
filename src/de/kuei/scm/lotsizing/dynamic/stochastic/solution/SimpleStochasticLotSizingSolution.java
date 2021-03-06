/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;

/**
 * A simple class which stores all solution data in fields
 * @author Andi Popp
 *
 */
public class SimpleStochasticLotSizingSolution extends
		AbstractStochasticLotSizingSolution {
	
	/**
	 * The total setup costs
	 */
	private double totalSetupCosts;
	
	/**
	 * The total inventory costs
	 */
	private double totalInventoryCosts;
	
	/**
	 * The amount variable name
	 */
	private String amountVariableName;
	
	/**
	 * The amount variable values
	 */
	private double[] amountVariableValues;
	
	/**
	 * The setup pattern
	 */
	private boolean[] setupPattern;
	
		

	/**
	 * Full parameter constructor
	 * @param totalSetupCosts
	 * @param totalInventoryCosts
	 * @param amountVariableName
	 * @param amountVariableValues
	 * @param setupPattern
	 * @throws IllegalArgumentException if the planning horizons of the amount variable values and the setup pattern do not match
	 */
	public SimpleStochasticLotSizingSolution(double totalSetupCosts, double totalInventoryCosts,
			String amountVariableName, double[] amountVariableValues,
			boolean[] setupPattern) {
		//Check inputs
		if (amountVariableValues.length != setupPattern.length) throw new IllegalArgumentException("Planning horizons did not match while constructing a simple stochastic lot sizing Solution");
		
		this.totalSetupCosts = totalSetupCosts;
		this.totalInventoryCosts = totalInventoryCosts;
		this.amountVariableName = amountVariableName;
		this.amountVariableValues = amountVariableValues;
		this.setupPattern = setupPattern;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution#getObjectiveValue()
	 */
	@Override
	public double getObjectiveValue() {
		return this.totalSetupCosts+this.totalInventoryCosts;
	}
	
	/**
	 * Gets the total setup costs
	 * @return the total setup costs
	 */
	public double getTotalSetupCosts() {
		return totalSetupCosts;
	}

	/**
	 * Gets the total inventory costs
	 * @return the total inventory costs
	 */
	public double getTotalInventoryCosts() {
		return totalInventoryCosts;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution#getAmountVariableValues()
	 */
	@Override
	public double[] getAmountVariableValues() {
		return this.amountVariableValues;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution#getAmountVariableName()
	 */
	@Override
	public String getAmountVariableName() {
		return this.amountVariableName;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution#getSetupPattern()
	 */
	@Override
	public boolean[] getSetupPattern() {
		return this.setupPattern;
	}

}
