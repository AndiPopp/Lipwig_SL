/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractLotSizingPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.DPBackwardRecursionPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.DPLotSizingDecision;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.LotsizingDPSolution;


/**
 * @author Andi Popp
 *
 */
public abstract class AbstractStochasticLotSizingDPSolver extends
		AbstractStochasticLotSizingSolver {

	/*
	 * (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolver#solve(de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem)
	 */
	@Override
	public AbstractStochasticLotSizingSolution solve(
			AbstractStochasticLotSizingProblem problem) {
		
		//First created the DP wrappers around the periods
		AbstractLotSizingPeriod[] originalPeriods = problem.getPeriods();
		DPBackwardRecursionPeriod[] periods = new DPBackwardRecursionPeriod[originalPeriods.length+1];
		//Create empty wrappers
		for (int i = 0; i < originalPeriods.length; i++) {
			periods[i] = new DPBackwardRecursionPeriod(originalPeriods[i]);
		}
		//Add the dummy period at the end
		periods[periods.length-1] = DPBackwardRecursionPeriod.getDummyPeriod();
		//Add the decisions
		for (int i = 0; i < periods.length-1; i++) {
			for (int j = i+1; j < periods.length; j++){
				try {
					periods[i].possibleDecisions.add(getDecision(periods, i, j, problem.getServiceLevel()));
				} catch (ConvolutionNotDefinedException e) {
					throw new RuntimeException("The demand distributions you have chosen could not be convoluted efficiently. Maybe try using normal distributions?");
				}
			}
		}
		
		//Solve the problem via backward recursion. Skip the dummy period.
		for (int i = periods.length-2; i >= 0; i--){
			try {
				periods[i].solve();
			} catch (SolvingInitialisiationException e) {
				throw new RuntimeException("The static uncertainty solver tried to solve an uninitialised period. This is a bug and should not happen");
			} catch (WrongOptimisationOrderException e) {
				throw new RuntimeException("The static uncertainty solver tried to solve the periods in the wrong order. This is a bug and should not happen");
			}
		}
		
		return new LotsizingDPSolution(periods, getAmoutVariableName());
	}
	
	/**
	 * Calculates the decision to produce a lot to cover periods setupPeriod to nextSetupPeriod-1
	 * @param periods The array of all the lot sizing periods wrapped in {@link DPBackwardRecursionPeriod}s 
	 * including the dummy period for the end of the planning horizon
	 * @param setupPeriod The index of the setup period
	 * @param nextSetupPeriod The index of the next setup period
	 * @param alpha The target alpha service level
	 * @return the decision
	 * @throws ConvolutionNotDefinedException if the demand distribution cannot be convoluted efficiently
	 */
	protected abstract DPLotSizingDecision getDecision(DPBackwardRecursionPeriod[] periods, int setupPeriod, int nextSetupPeriod, double alpha) throws ConvolutionNotDefinedException;

	/**
	 * Returns the amount variable name to put into the solution
	 */
	protected abstract String getAmoutVariableName();
}