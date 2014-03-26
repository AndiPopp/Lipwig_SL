/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic;

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
				csvString = csvString + orderDistributions[i].getNumericalMean()+";";
				csvString = csvString + orderDistributions[i].getNumericalVariance();
				if (i < orderDistributions.length-1) csvString = csvString+";";
		}
		
		return csvString;
	}
	
}
