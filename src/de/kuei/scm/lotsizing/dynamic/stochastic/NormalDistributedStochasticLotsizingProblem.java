/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.JOptionPane;

import de.kuei.scm.lotsizing.dynamic.stochastic.solver.AbstractStochasticLotSizingSolution;

/**
 * This class represents a single dynamic stochastic lot sizing problem with normal distributed
 * orders and therefore normal distributed aggregated demand.
 * @author Andi Popp
 *
 */
public class NormalDistributedStochasticLotsizingProblem extends AbstractStochasticLotSizingProblem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2954031522055879385L;

	/**
	 * The name of the problem
	 */
	String name;
	
	/**
	 * These are the lot sizing periods. They contain the demand distribution information,
	 * the setup cost parameter and the inventory holding cost parameter. See 
	 * {@link NormalDistributedLotSizingPeriod} for more details.
	 */
	NormalDistributedLotSizingPeriod[] periods;
		
	/**
	 * The target alpha service level for this problem
	 */
	float alpha;
	
	/**
	 * The collection of all solution calculated for this problem
	 * so far
	 */
	Vector<AbstractStochasticLotSizingSolution> solutions;
		
	/**
	 * Full parameter constructor
	 * @param name
	 * @param periods
	 * @param alpha
	 */
	public NormalDistributedStochasticLotsizingProblem(String name,
			NormalDistributedLotSizingPeriod[] periods, float alpha) {
		this.name = name;
		this.periods = periods;
		this.alpha = alpha;
		solutions = new Vector<AbstractStochasticLotSizingSolution>();
	}

	/**
	 * Constructs a problem from a csv file as created by {@link #toCSVFile(File)}. See the linked
	 * wiki page for further details.
	 * @see <a href="http://github.com/AndiPopp/Lipwig_SL/wiki/CSV-problem-file-format">CSV format wiki page</a>
	 * @param csvFile
	 */
	public NormalDistributedStochasticLotsizingProblem(File csvFile){
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile)));
			
			//Parse first line
			String firstLine = in.readLine();
			String[] firstLineValues = firstLine.split(";");
			this.name = firstLineValues[1];
			this.alpha = Float.parseFloat(firstLineValues[3]);
			
			//Discard the emtpy line
			in.readLine();
			//Discard the period headings
			in.readLine();
			
			//Read the period information
			Vector<NormalDistributedLotSizingPeriod> periodVector = new Vector<NormalDistributedLotSizingPeriod>();
			while(true){
				String periodString = in.readLine();
				if (periodString == null) break;
				periodVector.add(new NormalDistributedLotSizingPeriod(periodString));
			}
			this.periods = new NormalDistributedLotSizingPeriod[0];
			this.periods = periodVector.toArray(this.periods);
			
			//Close the buffered reader
			in.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not open "+csvFile.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public boolean toCSVFile(File csvFile){
		PrintStream out;
		try {
			out = new PrintStream(new FileOutputStream(csvFile));
			
			//write the alpha service level
			out.print("name:;"+this.name+";");
			out.print("alpha:;"+this.alpha+";");
			out.println("type:;NormalDistributed");
			
			//write an empty line to distinguish first line from period lines
			out.println();
			
			//Calculate the maximal order lead time
			int problemMaxNumberOfOrderPeriods = 0;
			for (int i = 0; i < periods.length;i++){
				if (periods[i].getNumberOfOrderPeriods()>problemMaxNumberOfOrderPeriods) problemMaxNumberOfOrderPeriods = periods[i].getNumberOfOrderPeriods();
			}
			
			//Write the order distribution parameter headline
			out.print("setup cost;inventory cost;");
			for (int i = 0; i < problemMaxNumberOfOrderPeriods; i++){
				out.print("mean "+i+";sd "+i);
				if (i < problemMaxNumberOfOrderPeriods-1) out.print(";");
			}
			out.println();
			
			//Write the order distribution parameters
			for (int i = 0; i < periods.length; i++){
				out.println(periods[i].toCSVString());
			}
			out.close();
		} catch (IOException e) {
			return false;
		}		
		return true;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public AbstractLotSizingPeriod[] getPeriods() {
		return this.periods;
	}
}
