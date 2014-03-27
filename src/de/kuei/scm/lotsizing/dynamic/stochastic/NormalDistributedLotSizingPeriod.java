/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic;

import java.util.Vector;

import de.kuei.scm.distribution.NormalDistribution;

import org.apache.commons.math3.distribution.RealDistribution;

import de.kuei.scm.distribution.Convoluter;

/**
 * @author Andi Popp
 *
 */
public class NormalDistributedLotSizingPeriod extends AbstractLotSizingPeriod {

	/**
	 * The field to actually save the object returned by 
	 * {@link AbstractLotSizingPeriod#getOrderDistributions()}
	 */
	private NormalDistribution[] orderDistributions;
	
	/**
	 * The full parameter constructor of this object
	 * @param setupCost
	 * @param inventoryHoldingCost
	 * @param orderDistributions
	 */
	public NormalDistributedLotSizingPeriod(double setupCost,
			double inventoryHoldingCost, NormalDistribution[] orderDistributions) {
		super(setupCost, inventoryHoldingCost);
		this.orderDistributions = orderDistributions;
	}

	/**
	 * Constructs a period object out of a single line in a csv file as created by
	 * the methid {@link #toCSVString()}.
	 * @param csvString
	 */
	public NormalDistributedLotSizingPeriod(String csvString){
		String[] values = csvString.split(";");
		this.setupCost = Double.parseDouble(values[0]);
		this.inventoryHoldingCost = Double.parseDouble(values[1]);
		Vector<NormalDistribution> orderDistributionVector = new Vector<NormalDistribution>();
		for (int i = 2; true; i+=2){
			double mean;
			try {
				if(values[i].equals("")) break;
				mean = Double.parseDouble(values[i]);
			} catch (ArrayIndexOutOfBoundsException e) {
				break;
			}
			double sd;
			try {
				sd = Double.parseDouble(values[i+1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new IllegalArgumentException("There is a mean without standard deviation in the period String:  "+csvString);
			}
			
			orderDistributionVector.add(new NormalDistribution(mean, sd));
		}
		orderDistributions = new NormalDistribution[0];
		this.orderDistributions = orderDistributionVector.toArray(this.orderDistributions);
	}
	
	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.AbstractLotSizingPeriod#getAggregatedDemandDistribution()
	 */
	@Override
	public RealDistribution getAggregatedDemandDistribution() {
		return (NormalDistribution) Convoluter.convolute(orderDistributions);
	}

	@Override
	public RealDistribution[] getOrderDistributions() {
		return this.orderDistributions;
	}

	
	
	/**
	 * This method turns the Period into a line which can be saved into a csv file. The
	 * format is as follows: The cells are sperated by a ';'. The first cell denotes the 
	 * setup cost parameter and the second one the inventory holdin cost parameter. The 
	 * following cells denote the order distributions represented by two cells with the
	 * mean and the standard deviation. The order cells are in the same sequence as in
	 * {@link NormalDistributedLotSizingPeriod#getOrderDistributions()}.
	 * @return a CSV string representation of the period
	 */
	public String toCSVString(){
		String csvString = "";
		
		//First cell is the setup cost parameter
		csvString = csvString + setupCost+";";
		
		//Second cell is the inventory cost parameter
		csvString = csvString + inventoryHoldingCost+";";
		
		//The next cells are the order distributions
		for (int i = 0; i < orderDistributions.length; i++){
				csvString = csvString + orderDistributions[i].getMean()+";";
				csvString = csvString + orderDistributions[i].getStandardDeviation();
				if (i < orderDistributions.length-1) csvString = csvString+";";
		}
		return csvString;
	}
	
}
