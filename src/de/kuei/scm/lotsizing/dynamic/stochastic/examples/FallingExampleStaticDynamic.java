/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.examples;

import java.io.File;

import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.NormalDistributedStochasticLotsizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.StaticDynamicUncertaintySimulator;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.SolvingInitialisiationException;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyULHSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.ArraysUtils;

/**
 * This example solves the problem described in Tempelmeier [2012] p. 319 with
 * the static uncertainty solver
 * @author Andi Popp
 *
 */
public class FallingExampleStaticDynamic {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Load the problem from csv file
		NormalDistributedStochasticLotsizingProblem problem = new NormalDistributedStochasticLotsizingProblem(new File("examples/example_problem_falling.csv"));
		
		//Create a static uncertainty solver
		AbstractStochasticLotSizingSolver solver = new StaticDynamicUncertaintyULHSolver();
		
		//Solve the problem with the solver
		AbstractStochasticLotSizingSolution solution = solver.solve(problem);
		
		//Print out the optimal value of the objective function and the value of the
		//decision variables
		System.out.println("NLH solution with optimal value: "+solution.getObjectiveValue());
		System.out.println(solution.getAmountVariableName()+":");
		double[] lotSizes = solution.getAmountVariableValues();
		for (int i = 0; i < lotSizes.length; i++){
			System.out.println(i+") "+ lotSizes[i]);
		}
		System.out.println();
		
		//Simulate the solution
		int n = 10000;
		double costs = 0.0;
		StaticDynamicUncertaintySimulator simulator = new StaticDynamicUncertaintySimulator();
		
		for (int i = 0; i < n; i++){
			try {
				costs += simulator.simulate(problem, solution);
			} catch (ConvolutionNotDefinedException e) {
				System.out.println(e);
			} catch (SolvingInitialisiationException e) {
				System.out.println(e);
			}
		}
		
		System.out.println("Average simulated costs: "+costs/n);

		double[] meanPeriodOvershoot = new double[problem.getPeriods().length];
		costs = 0.0;
		
		for (int i = 0; i < n; i++){
			try {
				double[] periodOvershoot = simulator.simulateBlockedOvershoot(problem, solution);
				for (int j = 0; j < periodOvershoot.length; j++){
					meanPeriodOvershoot[j] += periodOvershoot[j];
				}
				costs += simulator.simulateBlockedOvershootCosts(problem, solution);
			} catch (ConvolutionNotDefinedException e) {
				System.out.println(e);
			} catch (SolvingInitialisiationException e) {
				System.out.println(e);
			}
		}
		for (int j = 0; j < meanPeriodOvershoot.length; j++){
			meanPeriodOvershoot[j] = meanPeriodOvershoot[j] / n;
		}
		
		System.out.print("Average simulated blind stock:   ");
		ArraysUtils.printArray(meanPeriodOvershoot);
		
		
		try {
			System.out.print("Calculated expected blind stock: ");
			ArraysUtils.printArray(simulator.calculateExpectedBlockedOvershoot(problem, solution));
			System.out.println("Average simulated blind stock costs:   "+costs/n);
			System.out.println("Calculated expected blind stock costs: "+simulator.calculateExpectedBlockedOvershootCosts(problem, solution));
		} catch (ConvolutionNotDefinedException e) {
			System.out.println(e);
		} catch (SolvingInitialisiationException e) {
			System.out.println(e);
		}
		
		
	}

}
