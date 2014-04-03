/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.examples;

import java.io.File;

import de.kuei.scm.lotsizing.dynamic.stochastic.NormalDistributedStochasticLotsizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticUncertaintySolver;

/**
 * This example solves the problem described in Tempelmeier [2012] p. 319 with
 * the static uncertainty solver
 * @author Andi Popp
 *
 */
public class ExampleTempelmeier2012p319 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Load the problem from csv file
		NormalDistributedStochasticLotsizingProblem problem = new NormalDistributedStochasticLotsizingProblem(new File("examples/example_problem_Tempelmeier2012_p319.csv"));
		
		//Create a static uncertainty solver
		StaticUncertaintySolver solver = new StaticUncertaintySolver();
		
		//Solve the problem with the solver
		AbstractStochasticLotSizingSolution solution = solver.solve(problem);
		
		//Print out the optimal value of the objective function and the value of the
		//decision variables
		System.out.println("Solution with optimal value: "+solution.getObjectiveValue());
		System.out.println(solution.getAmountVariableName()+":");
		double[] lotSizes = solution.getAmountVariableValues();
		for (int i = 0; i < lotSizes.length; i++){
			System.out.println(i+") "+ lotSizes[i]);
		}
		
	}

}
