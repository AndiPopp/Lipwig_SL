/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;

import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractLotSizingPeriod;

/**
 * This class is a simple period solution wrapper which only stores the
 * values for the two decision variables (setup and amount)
 * @author Andi Popp
 *
 */
public class SimpleSolvedPeriod extends AbstractPeriodWrapper {

	/**
	 * The amount to be produced/produced up to/... in this period
	 */
	public double amount;
	
	/**
	 * The setup decision
	 */
	public boolean setup;
	
	
	/**
	 * Creates a wrapper around the specified period
	 * @param period the period to be wrapped
	 */
	public SimpleSolvedPeriod(AbstractLotSizingPeriod period) {
		super(period);
	}
	
	

}
