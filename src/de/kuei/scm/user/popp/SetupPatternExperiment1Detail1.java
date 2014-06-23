/**
 * 
 */
package de.kuei.scm.user.popp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.NormalDistributedStochasticLotsizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.ConstantNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.EqualDivisionNormalDistributedOrdersGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.NoisyNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.SinodialSeasonNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.TrendNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.TriangleDivisionNormalDistributedOrdersGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractLotSizingSimulator;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.StaticDynamicUncertaintySimulator;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.SolvingInitialisiationException;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyULHSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyWithADIULHSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.ArraysUtils;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.MultiPrintStreamWrapper;

/**
 * This executable class represents the cost effect experiment 1 from Popp [2014]
 * @author Andi Popp
 *
 */
public class SetupPatternExperiment1Detail1 {

	public static void main(String[] args) throws Exception {
		StaticDynamicUncertaintyULHSolver solver = new StaticDynamicUncertaintyULHSolver();
		StaticDynamicUncertaintyWithADIULHSolver solverADI = new StaticDynamicUncertaintyWithADIULHSolver();
		
		PrintStream filePrintStream = new PrintStream(new File("results/SetupPatternExperiment6.csv"));
		PrintStream[] printStreams = new PrintStream[2];
		printStreams[0] = System.out;
		printStreams[1] = filePrintStream;
		MultiPrintStreamWrapper out = new MultiPrintStreamWrapper(printStreams);
		
		out.println("s;Lagerkosten ohne ADI; Rüstkosten ohne ADI; #Rüstoperationen ohne ADI; Lagerkosten mit ADI; Rüstkosten mit ADI; #Rüstoperationen mit ADI; Rüstmuster gleich?");
		
		AbstractStochasticLotSizingProblem problem;
		AbstractStochasticLotSizingSolution solution;
		AbstractStochasticLotSizingSolution solutionADI;
		DecimalFormat oneDecimal = new DecimalFormat("0.0", new DecimalFormatSymbols(Locale.US));
		
		//Fixed problem parameters
		int T = 20;
		double cv = 0.8;
		float alpha = .95f;
		TriangleDivisionNormalDistributedOrdersGenerator orderGenerator = new TriangleDivisionNormalDistributedOrdersGenerator(8, 0);		
		
		for(double setupCost = 10; setupCost < 13000; setupCost += 10){
			
			out.print(setupCost);
			
			//Constant 100
			problem = ConstantNormalDistributedProblemGenerator.generate(T, 100, cv, setupCost, alpha, orderGenerator);
			solution = solver.solve(problem);
			solutionADI = solverADI.solve(problem);
			out.print(";"+solution.getTotalInventoryCosts(problem)+";"+solution.getTotalSetupCosts(problem)+";"+solution.getNumberOfSetupPeriods());
			out.print(";"+solutionADI.getTotalInventoryCosts(problem)+";"+solutionADI.getTotalSetupCosts(problem)+";"+solutionADI.getNumberOfSetupPeriods());
			out.print(";"+ArraysUtils.deepEquals(solution.getSetupPattern(), solutionADI.getSetupPattern()));
						
			out.println();
		}
		
		filePrintStream.flush();
		out.close();
	}

	

}
