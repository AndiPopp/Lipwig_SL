/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

/**
 * An Exception to be thrown when a solver cannot work because of missing
 * that has yet to be initialised.
 * @author Andi Popp
 *
 */
public class SolvingInitialisiationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2239331090332784467L;

	/**
	 * 
	 */
	public SolvingInitialisiationException() {
		super();
	}

	/**
	 * @param message
	 */
	public SolvingInitialisiationException(String message) {
		super(message);
	}

}
