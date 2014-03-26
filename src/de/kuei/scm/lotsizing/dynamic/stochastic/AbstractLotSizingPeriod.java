/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic;

import org.apache.commons.math3.distribution.RealDistribution;

/**
 * @author Andi Popp
 * A class representing a single period in a stochastic lot sizing problem.
 */
public abstract class AbstractLotSizingPeriod {
	
	
	
	/**
	 * @param setupCost The setup costs for this period
	 * @param inventoryHoldingCost inventoryHoldingCost;
	 */
	public AbstractLotSizingPeriod(double setupCost, double inventoryHoldingCost) {
		this.setupCost = setupCost;
		this.inventoryHoldingCost = inventoryHoldingCost;
	}

	/**
	 * The setup costs for this period
	 */
	protected double setupCost;
	
	/**
	 * Gets the value of the setup cost parameter
	 * @return the value of the setup cost parameter
	 */
	public double getSetupCost(){
		return setupCost;
	}
	
	/**
	 * The holding costs per ammount unit at the end of this period
	 */
	protected double inventoryHoldingCost;
	
	/**
	 * Gets the value of the inventory holding cost parameter
	 * @return the value of the inventory holding cost parameter
	 */
	public double getInventoryHoldingCost(){
		return inventoryHoldingCost;
	}
	
	/**
	 * The field returned by this method represents the adavance order information coming in. If t is
	 * the time index of the Period object, the {@link RealDistribution} object 
	 * at array position i represents the orders coming in at periode t-i.
	 * @return the distributions of the orders
	 */
	public abstract RealDistribution[] getOrderDistributions();
	
	/**
	 * This functions convolutes the {@link AbstractLotSizingPeriod#orderDistributions}
	 * into a single aggregated distribution.
	 * @return the distribution of the aggregated demand
	 */
	public abstract RealDistribution getAggregatedDemandDistribution();
	
}
