/**
 * 
 */
package de.kuei.scm.user.popp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.kuei.scm.lotsizing.dynamic.stochastic.NormalDistributedStochasticLotsizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.ConstantNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.EqualDivisionNormalDistributedDemandGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyNLHSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyWithADINLHSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.ArraysUtils;

/**
 * This executable class represents the cost effect experiment 1 from Popp [2014]
 * @author Andi Popp
 *
 */
public class CostEffectExperiment1 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		StaticDynamicUncertaintyNLHSolver solverWithoutADI = new StaticDynamicUncertaintyNLHSolver();
		StaticDynamicUncertaintyWithADINLHSolver solverWithADI = new StaticDynamicUncertaintyWithADINLHSolver();
		ConstantNormalDistributedProblemGenerator problemGenerator = new ConstantNormalDistributedProblemGenerator();
		EqualDivisionNormalDistributedDemandGenerator demandGenerator = new EqualDivisionNormalDistributedDemandGenerator(4, 0);
		FileWriter fileWriter = new FileWriter(new File("results/CostEffectExperiment1.csv"));
		
		System.out.println("Period;Setup patterns identical;TotalCostDifferenceWithoutADI;TotalCostDifferenceWithADI");
		fileWriter.write("Period;Setup patterns identical;TotalCostDifferenceWithoutADI;TotalCostDifferenceWithADI\n");
		for (int setupCost = 1; setupCost < 10000; setupCost++){
			NormalDistributedStochasticLotsizingProblem problem = problemGenerator.generate(20, 50.0, 25.0, setupCost*1.0, 1, 0.95f, demandGenerator);
			AbstractStochasticLotSizingSolution solutionWithoutADI = solverWithoutADI.solve(problem);
			AbstractStochasticLotSizingSolution solutionWithADI = solverWithADI.solve(problem);
			boolean setupEqual =  ArraysUtils.deepEquals(solutionWithADI.getSetupPattern(), solutionWithoutADI.getSetupPattern());
			String outString = setupCost+";"+setupEqual+";"
					+ (solutionWithoutADI.getTotalSetupCosts(problem)/solutionWithoutADI.getTotalInventoryCosts(problem))
					+";"+(solutionWithADI.getTotalSetupCosts(problem)/solutionWithADI.getTotalInventoryCosts(problem));
			System.out.println(outString);
			fileWriter.write(outString+"\n");
		}
		
		fileWriter.close();
	}

}
