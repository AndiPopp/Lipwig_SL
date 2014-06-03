/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;

import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.distribution.RealDistribution;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractLotSizingPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.SolvingInitialisiationException;

/**
 * @author Andi Popp
 *
 */
public class StaticDynamicUncertaintySimulator extends
		AbstractLotSizingSimulator {

	/**
	 * Amount of statistical mass that can be neglected for numerical calculations
	 */
	public double accuracy = 0.00000001;
	
	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractLotSizingSimulator#simulate(de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem, de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution)
	 */
	@Override
	public double simulate(AbstractStochasticLotSizingProblem problem,
			AbstractStochasticLotSizingSolution solution)
			throws ConvolutionNotDefinedException, SolvingInitialisiationException {
		
		//Check the inputs
		if (problem.getPeriods().length != solution.getAmountVariableValues().length)
			throw new SolvingInitialisiationException("The length of the problem and the solution do not match in StaticDynamicUncertaintySimulator solve");
		
		double CurrentInventory = 0.0; //Starting inventory
		double costs = 0.0;
		AbstractLotSizingPeriod[] periods = problem.getPeriods();
		boolean[] setupPattern = solution.getSetupPattern();
		double[] orderUpToLevel = solution.getAmountVariableValues();
		
		for (int i = 0; i < periods.length; i++){
			if (setupPattern[i]) costs += periods[i].getSetupCost();
			if (setupPattern[i] && CurrentInventory < orderUpToLevel[i]) CurrentInventory = orderUpToLevel[i];
			CurrentInventory -= periods[i].totalDemand().sample();
			costs += periods[i].getInventoryHoldingCost() * CurrentInventory;
		}
		
		return costs;
	}

	/**
	 * Simulates the blocked overshoot costs
	 * @param problem
	 * @param solution
	 * @return
	 * @throws ConvolutionNotDefinedException if the demand cannot be convoluted by {@link Convoluter}
	 * @throws SolvingInitialisiationException if problem and solution do not match
	 */
	public double simulateBlockedOvershootCosts(AbstractStochasticLotSizingProblem problem,
			AbstractStochasticLotSizingSolution solution)
			throws ConvolutionNotDefinedException, SolvingInitialisiationException {
		
		//Check the inputs
		if (problem.getPeriods().length != solution.getAmountVariableValues().length)
			throw new SolvingInitialisiationException("The length of the problem and the solution do not match in StaticDynamicUncertaintySimulator solve");
		
		double CurrentInventory = 0.0; //Starting inventory
		double overshoot = 0.0;
		double costs = 0.0;
		
		AbstractLotSizingPeriod[] periods = problem.getPeriods();
		boolean[] setupPattern = solution.getSetupPattern();
		double[] orderUpToLevel = solution.getAmountVariableValues();
		
		for (int i = 0; i < periods.length; i++){
			if (setupPattern[i]){
				if (CurrentInventory >= orderUpToLevel[i]) overshoot += CurrentInventory - orderUpToLevel[i];
				CurrentInventory = orderUpToLevel[i];
				costs += periods[i].getInventoryHoldingCost()*overshoot;
			}
			

		}
		
		return costs;
	}
	
	/**
	 * Calculates the blocked overshoot costs
	 * @param problem
	 * @param solution
	 * @return
	 * @throws ConvolutionNotDefinedException if the demand cannot be convoluted by {@link Convoluter}
	 * @throws SolvingInitialisiationException if problem and solution do not match
	 */
	public double calculateExpectedBlockedOvershootCosts(AbstractStochasticLotSizingProblem problem,
			AbstractStochasticLotSizingSolution solution)
			throws ConvolutionNotDefinedException, SolvingInitialisiationException {
		
		//Check the inputs
		if (problem.getPeriods().length != solution.getAmountVariableValues().length)
			throw new SolvingInitialisiationException("The length of the problem and the solution do not match in StaticDynamicUncertaintySimulator solve");
		
		double CurrentInventory = 0.0; //Starting inventory
		double overshoot = 0.0;
		double costs = 0.0;
		
		AbstractLotSizingPeriod[] periods = problem.getPeriods();
		boolean[] setupPattern = solution.getSetupPattern();
		double[] orderUpToLevel = solution.getAmountVariableValues();
		
		//No initial inventory so we skip the first period but have to start the demand with it
		RealDistribution demand = periods[0].totalDemand();
		int lastSetupPeriod = 0;
		
		for (int i = 1; i < periods.length; i++){
			if (setupPattern[i]){
				
				//Define the integrand as anonymous class
				final double zNminus1 = orderUpToLevel[lastSetupPeriod];
				final double zN = orderUpToLevel[i];
				final RealDistribution integrateDemand = demand;
				UnivariateFunction integrand = new UnivariateFunction() {
					@Override
					public double value(double q) {
						return (zNminus1 - zN - q)*integrateDemand.density(q);
					}
				};
				
				UnivariateIntegrator integrator = new SimpsonIntegrator();
				
				double min = demand.inverseCumulativeProbability(accuracy);
				double max = orderUpToLevel[lastSetupPeriod]-orderUpToLevel[i];
				overshoot += (integrator.integrate(1024, integrand, min, max)) ;
				
				//restart convoluting the demand 
				demand = periods[i].totalDemand();
			}
			else{
				demand = Convoluter.convolute(demand, periods[i].totalDemand());
			}
			costs += periods[i].getInventoryHoldingCost()*overshoot;
		}
		return costs;
	}
}
