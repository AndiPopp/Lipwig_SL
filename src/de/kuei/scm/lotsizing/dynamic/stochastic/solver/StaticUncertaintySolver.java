/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import org.apache.commons.math3.distribution.RealDistribution;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.distribution.NormalDistribution;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractLotSizingPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.StockFunction;

/**
 * This solver calculates the solution for a stochastic dynamic lot sizing solution with
 * static uncertainty strategy as proposed by Bookbinder & Tan [1988] with the modified
 * objective function proposed by Popp [2014].
 * @author Andi Popp
 *
 */
public class StaticUncertaintySolver extends AbstractStochasticLotSizingSolver {

	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = -2262997917688083174L;

	/* (non-Javadoc)
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
		
		return new StaticUncertaintySolution(periods);
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
	private DPLotSizingDecision getDecision(DPBackwardRecursionPeriod[] periods, int setupPeriod, int nextSetupPeriod, double alpha) throws ConvolutionNotDefinedException{
		//Get the vector of aggregated demand (there is not ADI in static uncertainty)
		RealDistribution[] demand = new RealDistribution[nextSetupPeriod];
		for (int i = 0; i < demand.length; i++){
			demand[i] = periods[i].period.getAggregatedDemandDistribution();
		}
		
		//Get the total production up to the beginning of the next setup period (i.e. exclusively!)
		RealDistribution totalDemandUpToNextSetupPeriod = Convoluter.convolute(demand);
		double totalProductionUpToNextSetupPeriod = totalDemandUpToNextSetupPeriod.inverseCumulativeProbability(alpha);
		
		//Start with the setup cost
		double decisionCost = periods[setupPeriod].period.getSetupCost();
		//Calculate and add the inventory cost
		for (int tau = setupPeriod; tau < nextSetupPeriod; tau++){
			RealDistribution totalDemandDistribution = Convoluter.convolute(demand, tau+1);
			StockFunction stockFunction = new StockFunction(totalDemandDistribution);
			decisionCost += periods[tau].period.getInventoryHoldingCost()*stockFunction.value(totalProductionUpToNextSetupPeriod);
		}

		//Calculate the production before the setup period to get the lot size
		double totalProductionUpToSetupPeriod = 0.0;
		if (setupPeriod > 0){
			RealDistribution totalDemandUpToSetupPeriod = Convoluter.convolute(demand, setupPeriod);
			totalProductionUpToSetupPeriod = totalDemandUpToSetupPeriod.inverseCumulativeProbability(alpha);
		}
		
		
		//return
		return new DPLotSizingDecision(periods[nextSetupPeriod], decisionCost, totalProductionUpToNextSetupPeriod-totalProductionUpToSetupPeriod);
	}

}
