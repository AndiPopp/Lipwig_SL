/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.SimpleStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.SetupPattern;

/**
 * A solver that enumerates all possible setup patterns and chooses the one with 
 * with the best objective value
 * @author Andi Popp
 *
 */
public abstract class AbstractStochasticLotSizingFullEnumerationSolver extends
		AbstractStochasticLotSizingSolver {

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolver#solve(de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem)
	 */
	@Override
	public AbstractStochasticLotSizingSolution solve(
			AbstractStochasticLotSizingProblem problem) {
		boolean[] pattern = new boolean[problem.getPeriods().length];
		pattern[0] = true;
		for (int i = 1; i < pattern.length; i++){
			pattern[i] = false;
		}
		
		SimpleStochasticLotSizingSolution bestSolution = null;
		
		while (true){
			try {
//				SetupPattern.printlnPattern(pattern);//DEBUG
				SimpleStochasticLotSizingSolution solution = solve(problem, pattern);
				
//				SetupPattern.printPattern(pattern);
//				System.out.println(" -> " + solution.getObjectiveValue());
		
				if (bestSolution == null || solution.getObjectiveValue() < bestSolution.getObjectiveValue())
					bestSolution = solution;
				pattern = SetupPattern.stepPattern(pattern);
			} catch (SolvingInitialisiationException ex) {
				//All patterns calculated
				break;
			} catch (ConvolutionNotDefinedException ex) {
				//TODO Convolution does not work
				break;
			}
		}
		
		return bestSolution;
	}
	
	/**
	 * Solves the given problem under static-dynamic uncertainty strategy for the given setup Pattern
	 * @param problem The problem to be solved with the given setup Pattern
	 * @param setupPattern The setup pattern the problem is to be solved for. First entry will always be interpreted as true,
	 * regardless of actual input
	 * @return the solution for this objective function
	 * @throws SolvingInitialisiationException if the setup pattern and the problem have different planning horizons
	 * @throws ConvolutionNotDefinedException If the {@link Convoluter} throws a {@link ConvolutionNotDefinedException} for the period demands
	 */
	public abstract SimpleStochasticLotSizingSolution solve(AbstractStochasticLotSizingProblem problem, boolean[] setupPattern) throws SolvingInitialisiationException, ConvolutionNotDefinedException;
	

}
