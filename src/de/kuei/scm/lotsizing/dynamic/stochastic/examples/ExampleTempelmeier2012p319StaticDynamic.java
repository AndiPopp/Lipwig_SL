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
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyNLHSolver;

/**
 * This example solves the problem described in Tempelmeier [2012] p. 319 with
 * the static uncertainty solver
 * @author Andi Popp
 *
 */
public class ExampleTempelmeier2012p319StaticDynamic {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Load the problem from csv file
		NormalDistributedStochasticLotsizingProblem problem = new NormalDistributedStochasticLotsizingProblem(new File("examples/example_problem_Tempelmeier2012_p319.csv"));
		
		//Create a static uncertainty solver
		AbstractStochasticLotSizingSolver solver = new StaticDynamicUncertaintyNLHSolver();
		
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
		
		
	}

}
