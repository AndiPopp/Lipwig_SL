/**
 * 
 */
package de.kuei.scm.user.popp;

import java.io.File;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.ConstantNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.EqualDivisionNormalDistributedOrdersGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.NoisyNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.SinodialSeasonNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.TrendNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.TriangleDivisionNormalDistributedOrdersGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyWithADIULHSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.MultiPrintStreamWrapper;

/**
 * @author Andi Popp
 *
 */
public class ADICostExperiment1b {
	
	public static void main(String[] args) throws Exception {
		StaticDynamicUncertaintyWithADIULHSolver solver = new StaticDynamicUncertaintyWithADIULHSolver();
		
		PrintStream filePrintStream = new PrintStream(new File("results/ADI-Cost-Experiment1b.csv"));
		PrintStream[] printStreams = new PrintStream[2];
		printStreams[0] = System.out;
		printStreams[1] = filePrintStream;
		MultiPrintStreamWrapper out = new MultiPrintStreamWrapper(printStreams);
		
		
		out.println("l;0.25;0.5;0.75;1.0;1.25;1.5;1.75;2.0");
		
		AbstractStochasticLotSizingProblem problem;
		AbstractStochasticLotSizingSolution solution;
		DecimalFormat lFormat = new DecimalFormat("00", new DecimalFormatSymbols(Locale.US));
		DecimalFormat costFormat = new DecimalFormat("#####0.00000", new DecimalFormatSymbols(Locale.US));

		
		//Fixed problem parameters
		int T = 20;
		float alpha = .95f;
		double setupCost = 2500;
		
		for(int l = 1; l < 21; l++){
			out.print(lFormat.format(l));
			TriangleDivisionNormalDistributedOrdersGenerator orderGenerator = new TriangleDivisionNormalDistributedOrdersGenerator(l, 0);
			
			//Constant(100)
			problem = ConstantNormalDistributedProblemGenerator.generate(T, 100, 0.25, setupCost, alpha, orderGenerator);
			solution = solver.solve(problem);
			out.print(";"+costFormat.format(solution.getObjectiveValue()));
			
			//Constant(100)
			problem = ConstantNormalDistributedProblemGenerator.generate(T, 100, 0.5, setupCost, alpha, orderGenerator);
			solution = solver.solve(problem);
			out.print(";"+costFormat.format(solution.getObjectiveValue()));

			//Constant(100)
			problem = ConstantNormalDistributedProblemGenerator.generate(T, 100, 0.75, setupCost, alpha, orderGenerator);
			solution = solver.solve(problem);
			out.print(";"+costFormat.format(solution.getObjectiveValue()));
			
			//Constant(100)
			problem = ConstantNormalDistributedProblemGenerator.generate(T, 100, 1.0, setupCost, alpha, orderGenerator);
			solution = solver.solve(problem);
			out.print(";"+costFormat.format(solution.getObjectiveValue()));
			
			//Constant(100)
			problem = ConstantNormalDistributedProblemGenerator.generate(T, 100, 1.25, setupCost, alpha, orderGenerator);
			solution = solver.solve(problem);
			out.print(";"+costFormat.format(solution.getObjectiveValue()));
			
			//Constant(100)
			problem = ConstantNormalDistributedProblemGenerator.generate(T, 100, 1.5, setupCost, alpha, orderGenerator);
			solution = solver.solve(problem);
			out.print(";"+costFormat.format(solution.getObjectiveValue()));
			
			//Constant(100)
			problem = ConstantNormalDistributedProblemGenerator.generate(T, 100, 1.75, setupCost, alpha, orderGenerator);
			solution = solver.solve(problem);
			out.print(";"+costFormat.format(solution.getObjectiveValue()));
			
			//Constant(100)
			problem = ConstantNormalDistributedProblemGenerator.generate(T, 100, 2.0, setupCost, alpha, orderGenerator);
			solution = solver.solve(problem);
			out.print(";"+costFormat.format(solution.getObjectiveValue()));
			
			
			out.println();
		}
		
		filePrintStream.flush();
		out.close();
	}

}
