/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractLotSizingPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.SolvingInitialisiationException;

/**
 * @author Andi Popp
 *
 */
public abstract class AbstractLotSizingSimulator implements BlindStockSimulator{

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
	
	public double calculateOvershootCosts(AbstractStochasticLotSizingProblem problem, double[] periodOvershoot){
		AbstractLotSizingPeriod[] periods = problem.getPeriods();
		double overshoot = 0.0;
		double costs = 0.0;
		for (int i = 0; i < periodOvershoot.length; i++){
			overshoot += periodOvershoot[i];
			costs += periods[i].getInventoryHoldingCost()*overshoot;
		}
		return costs;
	}
	
	public double simulateBlockedOvershootCosts(AbstractStochasticLotSizingProblem problem,
			AbstractStochasticLotSizingSolution solution)
			throws ConvolutionNotDefinedException, SolvingInitialisiationException {
		
		AbstractLotSizingPeriod[] periods = problem.getPeriods();
		double[] periodOvershoot = simulateBlockedOvershoot(problem, solution);
		double overshoot = 0.0;
		double costs = 0.0;

		for (int i = 0; i < periodOvershoot.length; i++){
			overshoot += periodOvershoot[i];
			costs += periods[i].getInventoryHoldingCost()*overshoot;
		}
		
		return costs;
	}
	
	public double calculateExpectedBlockedOvershootCosts(AbstractStochasticLotSizingProblem problem,
			AbstractStochasticLotSizingSolution solution)
			throws ConvolutionNotDefinedException, SolvingInitialisiationException {
		
		AbstractLotSizingPeriod[] periods = problem.getPeriods();
		double[] periodOvershoot = calculateExpectedBlockedOvershoot(problem, solution);
		double overshoot = 0.0;
		double costs = 0.0;

		for (int i = 0; i < periodOvershoot.length; i++){
			overshoot += periodOvershoot[i];
			costs += periods[i].getInventoryHoldingCost()*overshoot;
		}
		
		return costs;
	}
	
	
}
