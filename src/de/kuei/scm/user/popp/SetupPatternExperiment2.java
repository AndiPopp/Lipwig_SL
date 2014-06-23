/**
 * 
 */
package de.kuei.scm.user.popp;

import java.io.File;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.ConstantNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.TriangleDivisionNormalDistributedOrdersGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyULHSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyWithADIULHSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.ArraysUtils;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.MultiPrintStreamWrapper;

/**
 * This executable class represents the cost effect experiment 1 from Popp [2014]
 * @author Andi Popp
 *
 */
public class SetupPatternExperiment2 {

	public static void main(String[] args) throws Exception {
		StaticDynamicUncertaintyULHSolver solver = new StaticDynamicUncertaintyULHSolver();
		StaticDynamicUncertaintyWithADIULHSolver solverADI = new StaticDynamicUncertaintyWithADIULHSolver();
		
		PrintStream filePrintStream = new PrintStream(new File("results/SetupPatternExperiment2.csv"));
		PrintStream[] printStreams = new PrintStream[2];
		printStreams[0] = System.out;
		printStreams[1] = filePrintStream;
		MultiPrintStreamWrapper out = new MultiPrintStreamWrapper(printStreams);
		
		out.println(";CV=0.25;CV=0.25;CV=0.25;CV=0.5;CV=0.5;CV=0.5;CV=0.75;CV=0.75;CV=0.75;CV=1.0;CV=1.0;CV=1.0;CV=1.25;CV=1.25;CV=1.25;CV=1.5;CV=1.25;CV=1.25;;;;;;;");
		out.println("s;pattern identical?;more #setups without ADI?;more #setups with ADI?;pattern identical?;more #setups without ADI?;more #setups with ADI?;pattern identical?;more #setups without ADI?;more #setups with ADI?;pattern identical?;more #setups without ADI?;more #setups with ADI?;pattern identical?;more #setups without ADI?;more #setups with ADI?;pattern identical?;more #setups without ADI?;more #setups with ADI?");
		
		AbstractStochasticLotSizingProblem problem;
		AbstractStochasticLotSizingSolution solution;
		AbstractStochasticLotSizingSolution solutionADI;
		DecimalFormat oneDecimal = new DecimalFormat("0.0", new DecimalFormatSymbols(Locale.US));
		
		//Fixed problem parameters
		int T = 20;
		float alpha = .95f;
		TriangleDivisionNormalDistributedOrdersGenerator orderGenerator = new TriangleDivisionNormalDistributedOrdersGenerator(8, 0);		
		
		for(double setupCost = 10; setupCost < 13000; setupCost += 10){
			out.print(oneDecimal.format(setupCost));
			double marker = 0.5;
			
			for (double cv = .25; cv<1.55; cv += .25){
				//Constant(100)
				problem = ConstantNormalDistributedProblemGenerator.generate(T, 100, cv, setupCost, alpha, orderGenerator);
				solution = solver.solve(problem);
				solutionADI = solverADI.solve(problem);
				out.print(";");
				boolean equalSetupPattern = ArraysUtils.deepEquals(solution.getSetupPattern(), solutionADI.getSetupPattern());
				if (!equalSetupPattern && Math.abs(solverADI.solve(problem, solution.getSetupPattern()).getObjectiveValue() - solutionADI.getObjectiveValue()) < 0.0001) equalSetupPattern = true; 
				if (!equalSetupPattern) out.print(oneDecimal.format(marker));
				out.print(";");
				if (solution.getNumberOfSetupPeriods() > solutionADI.getNumberOfSetupPeriods()) out.print(oneDecimal.format(marker));
				out.print(";");
				if (solution.getNumberOfSetupPeriods() < solutionADI.getNumberOfSetupPeriods()) out.print(oneDecimal.format(marker));
				marker++;
			}
			
			
			out.println();
		}
		
		filePrintStream.flush();
		out.close();
	}

	

}
