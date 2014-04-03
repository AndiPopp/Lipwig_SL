/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic;

import java.io.File;
import java.io.Serializable;

/**
 * An abstract class from which lot sizing problems extend.
 * @author Andi Popp
 *
 */
public abstract class AbstractStochasticLotSizingProblem implements Serializable{
	
	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = -8380029432646482987L;

	/**
	 * Returns a name to identify the problem
	 * @return a name to identify the problem
	 */
	public abstract String getName();

	/**
	 * Writes the problem (without solutions) into a CSV file
	 * @param csvFile
	 * @return true if the problem was written to the file; false if an IOException occurs
	 */
	public abstract boolean toCSVFile(File csvFile);
	
	/**
	 * Gets the lot sizing periods of this problem
	 * @return the lot sizing periods of this problem
	 */
	public abstract AbstractLotSizingPeriod[] getPeriods();
	
	/**
	 * Gets the target alpha service level
	 * @return the target alpha service level
	 */
	public abstract double getServiceLevel();
	
	@Override
	public String toString(){
		return this.getName();
	}
	
}
