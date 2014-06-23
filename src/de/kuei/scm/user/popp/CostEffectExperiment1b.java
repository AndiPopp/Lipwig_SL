/**
 * 
 */
package de.kuei.scm.user.popp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.kuei.scm.lotsizing.dynamic.stochastic.NormalDistributedStochasticLotsizingProblem;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.ConstantNormalDistributedProblemGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.EqualDivisionNormalDistributedOrdersGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.generators.TriangleDivisionNormalDistributedOrdersGenerator;
import de.kuei.scm.lotsizing.dynamic.stochastic.solution.AbstractStochasticLotSizingSolution;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyULHSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.solver.StaticDynamicUncertaintyWithADIULHSolver;
import de.kuei.scm.lotsizing.dynamic.stochastic.util.ArraysUtils;

/**
 * This executable class represents the cost effect experiment 1 from Popp [2014]
 * @author Andi Popp
 *
 */
public class CostEffectExperiment1b {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		StaticDynamicUncertaintyULHSolver solverWithoutADI = new StaticDynamicUncertaintyULHSolver();
		StaticDynamicUncertaintyWithADIULHSolver solverWithADI = new StaticDynamicUncertaintyWithADIULHSolver();
		ConstantNormalDistributedProblemGenerator problemGenerator = new ConstantNormalDistributedProblemGenerator();
		TriangleDivisionNormalDistributedOrdersGenerator demandGenerator = new TriangleDivisionNormalDistributedOrdersGenerator(8, 0);
		FileWriter fileWriter = new FileWriter(new File("results/CostEffectExperiment1b.csv"));
		
		long start = System.currentTimeMillis();
		System.out.println("Period;Setup patterns identical;TotalCostQuotientWithoutADI;#Setups;TotalCostDifferenceWithADI;#Setups");
		fileWriter.write("Period;Setup patterns identical;TotalCostQuotientWithoutADI;#Setups;TotalCostDifferenceWithADI;#Setups\n");
		for (int setupCost = 5; setupCost < 7000; setupCost+=5){
			NormalDistributedStochasticLotsizingProblem problem = problemGenerator.generate(20, 50, 25.0, setupCost*1.0, 1, 0.95f, demandGenerator);
			AbstractStochasticLotSizingSolution solutionWithoutADI = solverWithoutADI.solve(problem);
			AbstractStochasticLotSizingSolution solutionWithADI = solverWithADI.solve(problem);
			boolean setupEqual =  ArraysUtils.deepEquals(solutionWithADI.getSetupPattern(), solutionWithoutADI.getSetupPattern());
			String outString = setupCost+";"+setupEqual
					+";"+ (solutionWithoutADI.getTotalSetupCosts(problem)/solutionWithoutADI.getTotalInventoryCosts(problem))
					+";"+ solutionWithoutADI.getNumberOfSetupPeriods()
					+";"+(solutionWithADI.getTotalSetupCosts(problem)/solutionWithADI.getTotalInventoryCosts(problem))
					+";"+ solutionWithADI.getNumberOfSetupPeriods();
			System.out.println(outString);
			fileWriter.write(outString+"\n");
		}
		System.out.println((System.currentTimeMillis()-start)/1000.0);
		
		fileWriter.close();
	}

}
