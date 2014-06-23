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

import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractStochasticLotSizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.AbstractNormalLikeOrdersGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.ConstantNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.EqualDivisionNormalDistributedOrdersGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.NoisyNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.SinodialSeasonNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.TrendNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.TriangleDivisionNormalDistributedOrdersGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractLotSizingSimulator;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.StaticDynamicUncertaintyWithADISimulator;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.SolvingInitialisiationException;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyWithADIULHSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.MultiPrintStreamWrapper;

/**
 * @author Andi Popp
 *
 */
public class ULHExperiment4b {
	
	public static void main(String[] args) throws Exception {
		StaticDynamicUncertaintyWithADIULHSolver solver = new StaticDynamicUncertaintyWithADIULHSolver();
		
		PrintStream filePrintStream = new PrintStream(new File("results/ULHExperiment4b.csv"));
		PrintStream[] printStreams = new PrintStream[2];
		printStreams[0] = System.out;
		printStreams[1] = filePrintStream;
		MultiPrintStreamWrapper out = new MultiPrintStreamWrapper(printStreams);
		
		
		out.println(";Constant(100);;Trend(80,120);;Trend(20,180);;Trend(120,80);;Trend(180,20);;Sinus(100,20);;Sinus(100,80);;Noise(U(80,120));;Noise(U(20,180))");
		out.println("CV;Simulation-Gap;Blocked-Overshoot-Gap;Simulation-Gap;Blocked-Overshoot-Gap;Simulation-Gap;Blocked-Overshoot-Gap;Simulation-Gap;Blocked-Overshoot-Gap;Simulation-Gap;Blocked-Overshoot-Gap;Simulation-Gap;Blocked-Overshoot-Gap;Simulation-Gap;Blocked-Overshoot-Gap;Simulation-Gap;Blocked-Overshoot-Gap;Simulation-Gap;Blocked-Overshoot-Gap;");
		
		AbstractStochasticLotSizingProblem problem;
		AbstractStochasticLotSizingSolution solution;
		StaticDynamicUncertaintyWithADISimulator simulator = new StaticDynamicUncertaintyWithADISimulator();
		DecimalFormat oneDecimal = new DecimalFormat("0.0", new DecimalFormatSymbols(Locale.US));
		int n = 10000;
		
		//Fixed problem parameters
		int T = 20;
		double setupCost = 2500;
		float alpha = .95f;
		AbstractNormalLikeOrdersGenerator orderGenerator = new TriangleDivisionNormalDistributedOrdersGenerator(8, 0);
		
		for(double cv = 0.3; cv < 2.1; cv += 0.1){
			out.print(oneDecimal.format(cv));
			
			//Constant(100)
			problem = ConstantNormalDistributedProblemGenerator.generate(T, 100, cv, setupCost, alpha, orderGenerator);
			solution = solver.solve(problem);
			writeSolutionData(problem, solution, out, n, simulator);
			
			//Trend(80,120)
			problem = TrendNormalDistributedProblemGenerator.generate(T, 80, 120, cv, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			writeSolutionData(problem, solution, out, n, simulator);
			
			//Trend(20,180)
			problem = TrendNormalDistributedProblemGenerator.generate(T, 20, 180, cv, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			writeSolutionData(problem, solution, out, n, simulator);
			
			//Trend(120,80)
			problem = TrendNormalDistributedProblemGenerator.generate(T, 120, 80, cv, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			writeSolutionData(problem, solution, out, n, simulator);
			
			//Trend(180,20)
			problem = TrendNormalDistributedProblemGenerator.generate(T, 180, 20, cv, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			writeSolutionData(problem, solution, out, n, simulator);
			
			//Sinus(100,20)
			problem = SinodialSeasonNormalDistributedProblemGenerator.generate(T/4, 4, 100, 20, cv, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			writeSolutionData(problem, solution, out, n, simulator);
			
			//Sinus(100,80)
			problem = SinodialSeasonNormalDistributedProblemGenerator.generate(T/4, 4, 100, 80, cv, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			writeSolutionData(problem, solution, out, n, simulator);
			
			//Noise(U(80,120))
			problem = NoisyNormalDistributedProblemGenerator.generate(T, new UniformRealDistribution(80, 120), cv, 1, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			writeSolutionData(problem, solution, out, n, simulator);
			
			//Noise(U(20,180))
			problem = NoisyNormalDistributedProblemGenerator.generate(T, new UniformRealDistribution(20, 180), cv, 1, setupCost, 1, alpha, orderGenerator);
			solution = solver.solve(problem);
			writeSolutionData(problem, solution, out, n, simulator);
			
			out.println();
		}
		
		filePrintStream.flush();
		out.close();
	}

	private static void writeSolutionData(AbstractStochasticLotSizingProblem problem, AbstractStochasticLotSizingSolution solution, PrintStream out, int n, AbstractLotSizingSimulator simulator) throws ConvolutionNotDefinedException, SolvingInitialisiationException{
		double simulatedCosts = 0.0;
		for (int i = 0; i < n; i++){
			simulatedCosts += simulator.simulate(problem, solution);
		}
		double simGap = (((simulatedCosts/n)-solution.getObjectiveValue())/solution.getObjectiveValue());
		assert simGap < 0.01;
		if (simGap < 0) simGap = 0;
		double bosGap = (simulator.calculateExpectedBlockedOvershootCosts(problem, solution)/solution.getObjectiveValue());
		DecimalFormat format = new DecimalFormat("#####0.00000", new DecimalFormatSymbols(Locale.US));
		out.print(";"+format.format(simGap)+";"+format.format(bosGap));
	}
}
