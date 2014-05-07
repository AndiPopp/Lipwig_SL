/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;

import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractLotSizingPeriod;

/**
 * This class is an abstract class from which wrappers around {@link AbstractLotSizingPeriod} objects
 * to represent the solution can be extended.
 * @author Andi Popp
 *
 */
public abstract class AbstractPeriodWrapper {

	/**
	 * The wrapped lot sizing period
	 */
	public final AbstractLotSizingPeriod period;

	/**
	 * @param period
	 */
	public AbstractPeriodWrapper(AbstractLotSizingPeriod period) {
		this.period = period;
	}
	
}
