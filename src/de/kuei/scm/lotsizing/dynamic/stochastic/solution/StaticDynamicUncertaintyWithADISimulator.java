/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.solution;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.distribution.RealDistribution;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractLotSizingPeriod;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.SolvingInitialisiationException;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.SimpleSimpsonIntegrator;

/**
 * @author Andi Popp
 *
 */
public class StaticDynamicUncertaintyWithADISimulator extends
		AbstractLotSizingSimulator {
	
	/**
	 * Amount of statistical mass that can be neglected for numerical calculations
	 */
	public double accuracy = 0.00000001;
	
	/**
	 * simulation parameter
	 */
	private double CurrentInventory ; 
	/**
	 * simulation parameter
	 */
	private double costs;
	/**
	 * simulation parameter
	 */
	private AbstractLotSizingPeriod[] periods;
	/**
	 * simulation parameter
	 */
	private boolean[] setupPattern;
	/**
	 * simulation parameter
	 */
	private double[] orderUpToLevelForOpenDemand;
	/**
	 * simulation parameter
	 */
	private double[][] orders;
	/**
	 * simulation parameter
	 */
	private double[] totalOrders;
	/**
	 * simulation parameter
	 */
	private Double[] realizedDemand;
	
	/**
	 * Initializes all the parameters for the current simulation
	 * @param problem
	 * @param solution
	 * @throws SolvingInitialisiationException
	 */
	private void initializeSimulationParameters(AbstractStochasticLotSizingProblem problem,
			AbstractStochasticLotSizingSolution solution) throws SolvingInitialisiationException{
		
		//Check the inputs
		if (problem.getPeriods().length != solution.getAmountVariableValues().length)
			throw new SolvingInitialisiationException("The length of the problem and the solution do not match in StaticDynamicUncertaintySimulator solve");
				
		CurrentInventory = 0.0; //Starting inventory
		costs = 0.0;
		periods = problem.getPeriods();
		setupPattern = solution.getSetupPattern();
		orderUpToLevelForOpenDemand = solution.getAmountVariableValues();
		
		//Presampling orders
		orders = new double[periods.length][];
		totalOrders = new double[periods.length];
		for (int i = 0; i < orders.length; i++){
			orders[i] = new double[periods[i].getOrderDistributions().length];
			totalOrders[i] = 0.0;
			for (int j = 0; j < orders[i].length; j++){
				orders[i][j] = periods[i].getOrderDistributions()[j].sample();
				totalOrders[i] += orders[i][j];
			}
		}
		
		//Precalculating the realized demand
		realizedDemand = new Double[periods.length];
		for (int t = 0; t < realizedDemand.length; t++){
			if (setupPattern[t]){
				realizedDemand[t] = 0.0;
				int setupPeriod = t;
				int nextSetupPeriod = t+1;
				while(nextSetupPeriod < setupPattern.length && !setupPattern[nextSetupPeriod]){
					nextSetupPeriod++;
				}
				
				for (int tau = setupPeriod; tau < nextSetupPeriod; tau++){
					realizedDemand[t] += getRealizedDemand(orders[tau], tau, setupPeriod);
				}
			}
			else{
				realizedDemand[t] = null;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractLotSizingSimulator#simulate(de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem, de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution)
	 */
	@Override
	public double simulate(AbstractStochasticLotSizingProblem problem,
			AbstractStochasticLotSizingSolution solution)
			throws ConvolutionNotDefinedException,
			SolvingInitialisiationException {
		
		initializeSimulationParameters(problem, solution);
					
		for (int t = 0; t < periods.length; t++){
			if (setupPattern[t]) {
				costs += periods[t].getSetupCost();
				double orderUpToLevel;
				try {
					orderUpToLevel = orderUpToLevelForOpenDemand[t] + realizedDemand[t];
				} catch (NullPointerException e) {
					throw new NullPointerException("Realized demand was null for period "+t+" in StaticDynamicUncertaintyWithADISimulator.solve(). This is probably due to "+t+" not being a setup period.");
				}
				if (CurrentInventory < orderUpToLevel) CurrentInventory = orderUpToLevel;
			}
			CurrentInventory -= totalOrders[t];
			costs += periods[t].getInventoryHoldingCost() * CurrentInventory;
		}
		
		return costs;
		
	}

	/**
	 * Calculates the sum of orders already realized in the setup period for a given array of sampled orders for a period
	 * @param orders the array of sampled orders
	 * @param period the period the orders are placed for
	 * @param setupPeriod the setup period for the period
	 * @return
	 */
	private double getRealizedDemand(double[] orders, int period, int setupPeriod){
		int timeDifference = period-setupPeriod;
		double realizedOrders = .0;
		for (int i = timeDifference+1;i < orders.length; i++){
			realizedOrders += orders[i];
		}
		return realizedOrders;
	}

	/*
	 * (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solution.BlindStockSimulator#simulateBlockedOvershoot(de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem, de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution)
	 */
	@Override
	public double[] simulateBlockedOvershoot(
			AbstractStochasticLotSizingProblem problem,
			AbstractStochasticLotSizingSolution solution)
			throws ConvolutionNotDefinedException,
			SolvingInitialisiationException {
		
		initializeSimulationParameters(problem, solution);
		
		double[] blindStock = new double[periods.length];
		
		for (int t = 0; t < periods.length; t++){
			if (setupPattern[t]) {
				double orderUpToLevel;
				try {
					orderUpToLevel = orderUpToLevelForOpenDemand[t] + realizedDemand[t];
				} catch (NullPointerException e) {
					throw new NullPointerException("Realized demand was null for period "+t+" in StaticDynamicUncertaintyWithADISimulator.solve(). This is probably due to "+t+" not being a setup period.");
				}
				if (CurrentInventory > orderUpToLevel) blindStock[t] = CurrentInventory-orderUpToLevel;
				else blindStock[t] = 0.0;
				CurrentInventory = orderUpToLevel;
			}
			CurrentInventory -= totalOrders[t];
		}
		
		return blindStock; 
		
	}

	/*
	 * (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.solution.BlindStockSimulator#calculateExpectedBlockedOvershoot(de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem, de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution)
	 */
	@Override
	public double[] calculateExpectedBlockedOvershoot(AbstractStochasticLotSizingProblem problem,
			AbstractStochasticLotSizingSolution solution)
			throws ConvolutionNotDefinedException, SolvingInitialisiationException {
		
		//Check the inputs
		if (problem.getPeriods().length != solution.getAmountVariableValues().length)
			throw new SolvingInitialisiationException("The length of the problem and the solution do not match in StaticDynamicUncertaintySimulator solve");
		
		
		
		AbstractLotSizingPeriod[] periods = problem.getPeriods();
		boolean[] setupPattern = solution.getSetupPattern();
		double[] orderUpToLevelForOpenDemand = solution.getAmountVariableValues();
		
		double[] periodOvershoot = new double[periods.length];
		
		//No initial inventory so we skip the first period but have to start the demand with it
		int lastSetupPeriod = 0;
		RealDistribution openDemand = periods[0].openDemand(0, lastSetupPeriod);
		
		
		for (int i = 1; i < periods.length; i++){
			if (setupPattern[i]){
				//calculate the realised demand
				RealDistribution realizedDemand = periods[i].realisedDemand(i, i);
				for(int j = i+1; j < periods.length && !setupPattern[j]; j++){
					realizedDemand = Convoluter.convolute(realizedDemand, periods[i].realisedDemand(j, i));
				}
								
				//Define the integrand as anonymous class
				final double zNminus1 = orderUpToLevelForOpenDemand[lastSetupPeriod];
				final double zN = orderUpToLevelForOpenDemand[i];
				final RealDistribution integrateDemand = Convoluter.convolute(openDemand, realizedDemand);
				UnivariateFunction integrand = new UnivariateFunction() {
					@Override
					public double value(double q) {
						return (zNminus1 - zN - q)*integrateDemand.density(q);
					}
				};
				
//				UnivariateIntegrator integrator = new SimpsonIntegrator();
				UnivariateIntegrator integrator = new SimpleSimpsonIntegrator();
				
				double min = integrateDemand.inverseCumulativeProbability(accuracy);
				double max = orderUpToLevelForOpenDemand[lastSetupPeriod]-orderUpToLevelForOpenDemand[i];
				periodOvershoot[i] = (integrator.integrate(4096, integrand, min, max));
						
				//restart convoluting the open demand 
				lastSetupPeriod = i;
				openDemand = periods[i].openDemand(i, lastSetupPeriod);
				
				
			}
			else{
				openDemand = Convoluter.convolute(openDemand, periods[i].openDemand(i, lastSetupPeriod));
				
			}
		}
		return periodOvershoot;
	}
}
