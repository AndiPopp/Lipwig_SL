/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.SolvingInitialisiationException;

/**
 * @author Andi Popp
 *
 */
public abstract class AbstractLotSizingSimulator {

	/**
	 * Does on replication of a simulation of the problem with the given solution
	 * @param problem
	 * @param solution
	 * @return the total costs in this replication
	 * @throws ConvolutionNotDefinedException if the demand can not be convoluted by {@link Convoluter}
	 * @throws SolvingInitialisiationException if the solution does not fit the problem
	 */
	public abstract double simulate(
			AbstractStochasticLotSizingProblem problem, 
			AbstractStochasticLotSizingSolution solution)
		throws ConvolutionNotDefinedException, SolvingInitialisiationException;
}
