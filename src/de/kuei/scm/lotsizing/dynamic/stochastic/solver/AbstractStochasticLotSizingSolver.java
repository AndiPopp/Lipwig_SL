/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import java.io.Serializable;

import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution;

/**
 * An abstract class for lot sizing solvers. Each solver implements a different algorithm 
 * to actually get a possible solution for a stochastic lot sizing problem.
 * @author Andi Popp
 *
 */
public abstract class AbstractStochasticLotSizingSolver implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6470126286341145310L;

	public abstract AbstractStochasticLotSizingSolution solve(AbstractStochasticLotSizingProblem problem);
	
}
