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
import de.kuei.scm.lotsizing.dynamic.stochastic.util.SetupPattern;

/**
 * This executable class represents the cost effect experiment 1 from Popp [2014]
 * @author Andi Popp
 *
 */
public class SetupPatternExperiment1Detail2 {

	public static void main(String[] args) throws Exception {
		StaticDynamicUncertaintyULHSolver solver = new StaticDynamicUncertaintyULHSolver();
		StaticDynamicUncertaintyWithADIULHSolver solverADI = new StaticDynamicUncertaintyWithADIULHSolver();
		
		PrintStream filePrintStream = new PrintStream(new File("results/SetupPatternExperiment1_D2.csv"));
		PrintStream[] printStreams = new PrintStream[2];
		printStreams[0] = System.out;
		printStreams[1] = filePrintStream;
		MultiPrintStreamWrapper out = new MultiPrintStreamWrapper(printStreams);
		
		out.println(";Constant(100);;;Trend(80,120);;;Trend(20,180);;;Trend(120,80);;;Trend(180,20);;;Sinus(80-120);;;Sinus(20-180);;;Noise(U(80,120));;;Noise(U(20-180))");
		out.println("s;pattern identical?;more #setups without ADI?;more #setups with ADI?;pattern identical?;more #setups without ADI?;more #setups with ADI?;pattern identical?;more #setups without ADI?;more #setups with ADI?;pattern identical?;more #setups without ADI?;more #setups with ADI?;pattern identical?;more #setups without ADI?;more #setups with ADI?;pattern identical?;more #setups without ADI?;more #setups with ADI?;pattern identical?;more #setups without ADI?;more #setups with ADI?;pattern identical?;more #setups without ADI?;more #setups with ADI?;pattern identical?;more #setups without ADI?;more #setups with ADI?");
		
		AbstractStochasticLotSizingProblem problem;
		AbstractStochasticLotSizingSolution solution;
		AbstractStochasticLotSizingSolution solutionADI;
		DecimalFormat oneDecimal = new DecimalFormat("0.0", new DecimalFormatSymbols(Locale.US));
		
		//Fixed problem parameters
		int T = 20;
		double cv = 0.8;
		float alpha = .95f;
		TriangleDivisionNormalDistributedOrdersGenerator orderGenerator = new TriangleDivisionNormalDistributedOrdersGenerator(8, 0);		
		
			
			double setupCost = 3750;
		
			//Trend(20,180)
			System.out.println("Trend(20,180); s="+setupCost);
			problem = TrendNormalDistributedProblemGenerator.generate(T, 20, 180, cv, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			solutionADI = solverADI.solve(problem);
			System.out.println("Cost difference: "+Math.abs(solverADI.solve(problem, solution.getSetupPattern()).getObjectiveValue() - solutionADI.getObjectiveValue()));
			SetupPattern.printlnPattern(solution.getSetupPattern());
			SetupPattern.printlnPattern(solutionADI.getSetupPattern());
			
			//Trend(180,20)
			System.out.println("Trend(180,20); s="+setupCost);
			problem = TrendNormalDistributedProblemGenerator.generate(T, 180, 20, cv, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			solutionADI = solverADI.solve(problem);
			System.out.println("Cost difference: "+Math.abs(solverADI.solve(problem, solution.getSetupPattern()).getObjectiveValue() - solutionADI.getObjectiveValue()));
			SetupPattern.printlnPattern(solution.getSetupPattern());
			SetupPattern.printlnPattern(solutionADI.getSetupPattern());
			
			//Sinus(100,80)
			System.out.println("Sinus(100,80); s="+setupCost);
			problem = SinodialSeasonNormalDistributedProblemGenerator.generate(T/4, 4, 100, 80, cv, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			solutionADI = solverADI.solve(problem);
			System.out.println("Cost difference: "+Math.abs(solverADI.solve(problem, solution.getSetupPattern()).getObjectiveValue() - solutionADI.getObjectiveValue()));
			SetupPattern.printlnPattern(solution.getSetupPattern());
			SetupPattern.printlnPattern(solutionADI.getSetupPattern());
		
			//Noise(U(80,120))
			System.out.println("Noise(U(80,120)); s="+setupCost);
			problem = NoisyNormalDistributedProblemGenerator.generate(T, new UniformRealDistribution(80, 120), cv, 1, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			solutionADI = solverADI.solve(problem);
			System.out.println("Cost difference: "+Math.abs(solverADI.solve(problem, solution.getSetupPattern()).getObjectiveValue() - solutionADI.getObjectiveValue()));
			SetupPattern.printlnPattern(solution.getSetupPattern());
			SetupPattern.printlnPattern(solutionADI.getSetupPattern());

			
			setupCost = 8000;
			
			//Trend(20,180)
			System.out.println("Trend(20,180); s="+setupCost);
			problem = TrendNormalDistributedProblemGenerator.generate(T, 20, 180, cv, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			solutionADI = solverADI.solve(problem);
			System.out.println("Cost difference: "+Math.abs(solverADI.solve(problem, solution.getSetupPattern()).getObjectiveValue() - solutionADI.getObjectiveValue()));
			SetupPattern.printlnPattern(solution.getSetupPattern());
			SetupPattern.printlnPattern(solutionADI.getSetupPattern());
			
			//Trend(180,20)
			System.out.println("Trend(180,20); s="+setupCost);
			problem = TrendNormalDistributedProblemGenerator.generate(T, 180, 20, cv, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			solutionADI = solverADI.solve(problem);
			System.out.println("Cost difference: "+Math.abs(solverADI.solve(problem, solution.getSetupPattern()).getObjectiveValue() - solutionADI.getObjectiveValue()));
			SetupPattern.printlnPattern(solution.getSetupPattern());
			SetupPattern.printlnPattern(solutionADI.getSetupPattern());
			
			//Sinus(100,80)
			System.out.println("Sinus(100,80); s="+setupCost);
			problem = SinodialSeasonNormalDistributedProblemGenerator.generate(T/4, 4, 100, 80, cv, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			solutionADI = solverADI.solve(problem);
			System.out.println("Cost difference: "+Math.abs(solverADI.solve(problem, solution.getSetupPattern()).getObjectiveValue() - solutionADI.getObjectiveValue()));
			SetupPattern.printlnPattern(solution.getSetupPattern());
			SetupPattern.printlnPattern(solutionADI.getSetupPattern());
		
			//Noise(U(80,120))
			System.out.println("Noise(U(80,120)); s="+setupCost);
			problem = NoisyNormalDistributedProblemGenerator.generate(T, new UniformRealDistribution(80, 120), cv, 1, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			solutionADI = solverADI.solve(problem);
			System.out.println("Cost difference: "+Math.abs(solverADI.solve(problem, solution.getSetupPattern()).getObjectiveValue() - solutionADI.getObjectiveValue()));
			SetupPattern.printlnPattern(solution.getSetupPattern());
			SetupPattern.printlnPattern(solutionADI.getSetupPattern());
			
			out.println();
		
		
		filePrintStream.flush();
		out.close();
	}

	

}
