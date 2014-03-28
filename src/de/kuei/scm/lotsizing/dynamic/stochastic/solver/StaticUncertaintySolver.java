/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;

/**
 * @author Andi Popp
 *
 */
public class StaticUncertaintySolver extends AbstractStochasticLotSizingSolver {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2262997917688083174L;

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolver#solve(de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem)
	 */
	@Override
	public AbstractStochasticLotSizingSolution solve(
			AbstractStochasticLotSizingProblem problem) {
		
		//First created the DP wrappers around the periods
		DPBackwardRecursionPeriod[] periods = new DPBackwardRecursionPeriod[problem.getPeriods().length];
		for (int i = 0; i < periods.length; i++) {
			//Create the wrapper
			periods[i] = new DPBackwardRecursionPeriod(problem.getPeriods()[i]);
			//TODO kannten
		}
		
		//TODO
		return null;
	}
	
	public double getDecisionCost(int setupPeriod, int nextSetupPeriod){
		
		//TODO
		return 0.0;
	}

}
