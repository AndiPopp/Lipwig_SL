/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution;

/**
 * An abstract class for lot sizing solvers. Each solver implements a different algorithm 
 * to actually get a possible solution for a stochastic lot sizing problem.
 * @author Andi Popp
 *
 */
public abstract class AbstractStochasticLotSizingSolver {


	public abstract AbstractStochasticLotSizingSolution solve(AbstractStochasticLotSizingProblem problem);
	
	public abstract AbstractStochasticLotSizingSolution solve(AbstractStochasticLotSizingProblem problem, boolean[] setupPattern) throws SolvingInitialisiationException, ConvolutionNotDefinedException;
}
