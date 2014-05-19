package de.kuei.scm.lotsizing.dynamic.stochastic.examples;

import java.io.File;

import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.NormalDistributedStochasticLotsizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyNLHSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyWithADINLHSolver;

public class Example2WithAOI {

	public static void main(String[] args) throws ConvolutionNotDefinedException {
		NormalDistributedStochasticLotsizingProblem problem = new NormalDistributedStochasticLotsizingProblem(new File("examples/example_problem2.csv"));
		
		AbstractStochasticLotSizingSolver s = new StaticDynamicUncertaintyNLHSolver();
		AbstractStochasticLotSizingSolution solution = s.solve(problem);
		solution.print();
		System.out.println("Total Setup costs: "+solution.getTotalSetupCosts(problem));
		System.out.println("Total Inventory costs: "+solution.getTotalInventoryCosts(problem));
		
		System.out.println();
		
		s = new StaticDynamicUncertaintyWithADINLHSolver();
		solution = s.solve(problem);
		solution.print();
		System.out.println("Total Setup costs: "+solution.getTotalSetupCosts(problem));
		System.out.println("Total Inventory costs: "+solution.getTotalInventoryCosts(problem));
		
		
		
	}
	
}
