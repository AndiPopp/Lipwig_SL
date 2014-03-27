/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * This class represents a single dynamic stochastic lot sizing problem with normal distributed
 * orders and therefore normal distributed aggregated demand.
 * @author Andi Popp
 *
 */
public class NormalDistributedStochasticDynamicLotsizingProblem implements Serializable{
	
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
	Vector<AbstractStochasticDynamicLotSizingSolution> solutions;
	
	
	
	/**
	 * Full parameter constructor
	 * @param name
	 * @param periods
	 * @param alpha
	 */
	public NormalDistributedStochasticDynamicLotsizingProblem(String name,
			NormalDistributedLotSizingPeriod[] periods, float alpha) {
		this.name = name;
		this.periods = periods;
		this.alpha = alpha;
		solutions = new Vector<AbstractStochasticDynamicLotSizingSolution>();
	}

	/**
	 * Constructs a problem from a csv file as created by {@link #toCSVFile(File)}
	 * @param csvFile
	 */
	public NormalDistributedStochasticDynamicLotsizingProblem(File csvFile){
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


	/**
	 * 
	 * @param csvFile
	 * @return true if the problem was written to the file; false if an IOException occurs
	 */
	public boolean toCSVFile(File csvFile){
		PrintStream out;
		try {
			out = new PrintStream(new FileOutputStream(csvFile));
			
			//write the alpha service level
			out.print("name:;"+this.name+";");
			out.println("alpha:;"+this.alpha);
			
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
	public String toString(){
		return this.name;
	}
}
