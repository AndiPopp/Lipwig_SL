/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.SolvingInitialisiationException;

/**
 * Classes that implement this interface can simulate and calculate the blind stock for a given
 * solution
 * @author Andi Popp
 *
 */
public interface BlindStockSimulator {

	/**
	 * Simulates the blocked overshoot 
	 * @param problem
	 * @param solution
	 * @return
	 * @throws ConvolutionNotDefinedException if the demand cannot be convoluted by {@link Convoluter}
	 * @throws SolvingInitialisiationException if problem and solution do not match
	 */
	public double[] simulateBlockedOvershoot(AbstractStochasticLotSizingProblem problem,
			AbstractStochasticLotSizingSolution solution)
			throws ConvolutionNotDefinedException, SolvingInitialisiationException;
	
	/**
	 * Calculates the blocked overshoot costs
	 * @param problem
	 * @param solution
	 * @return
	 * @throws ConvolutionNotDefinedException if the demand cannot be convoluted by {@link Convoluter}
	 * @throws SolvingInitialisiationException if problem and solution do not match
	 */
	public abstract double[] calculateExpectedBlockedOvershoot(AbstractStochasticLotSizingProblem problem,
			AbstractStochasticLotSizingSolution solution)
			throws ConvolutionNotDefinedException, SolvingInitialisiationException;
	
	
	
}
