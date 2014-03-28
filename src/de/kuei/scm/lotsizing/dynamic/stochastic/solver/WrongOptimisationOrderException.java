/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

/**
 * An exception to be thrown if the optimisation is in the wrong
 * order, e.g. no strict backwarts recursion
 * @author Andi Popp
 *
 */
public class WrongOptimisationOrderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3376884944161034320L;

	/**
	 * 
	 */
	public WrongOptimisationOrderException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public WrongOptimisationOrderException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
