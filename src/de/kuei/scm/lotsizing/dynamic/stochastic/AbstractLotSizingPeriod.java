/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic;

import java.io.Serializable;

import org.apache.commons.math3.distribution.RealDistribution;

import de.kuei.scm.distribution.Convoluter;
import de.kuei.scm.distribution.ConvolutionNotDefinedException;
import de.kuei.scm.distribution.RealSinglePointDistribution;

/**
 * A class representing a single period in a stochastic lot sizing problem. In the following
 * documentation the time index of this period is denoted by t.
 * 
 * This class is used to store the parameters of the Period. To represent the solution it is
 * to be put into a appropriate wrapper class as found in the package 
 * de.kue.scm.lotsizing.dynamic.stochastic.solution
 * @author Andi Popp
 */
public abstract class AbstractLotSizingPeriod implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5423156534006656229L;

	/**
	 * Empty constructor
	 */
	public AbstractLotSizingPeriod(){}
	
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
	 * The holding costs per amount unit at the end of this period
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
	 * The field returned by this method represents the advance order information coming in. 
	 * The {@link RealDistribution} object 
	 * at array position i represents the orders coming in at period t-i.
	 * @return the distributions of the orders
	 */
	public abstract RealDistribution[] getOrderDistributions();
	
	/**
	 * This method returns the maximal order leadtime (denoted by l_max in Popp [2014]) for
	 * this period. 
	 * @return the maximal order lead time for this period
	 */
	public int getMaxOrderLeadTime(){
		return getOrderDistributions().length-1;
	}
	
	/**
	 * This method returns the actual number of order periods, including the period itself
	 * @return the actual number of order periods
	 */
	public int getNumberOfOrderPeriods(){
		return getOrderDistributions().length;
	}
	
	/**
	 * This functions convolutes the {@link AbstractLotSizingPeriod#orderDistributions}
	 * into a single aggregated distribution.
	 * @return the distribution of the aggregated demand
	 */
	public abstract RealDistribution totalDemand();
	
	/**
	 * This method convolutes the all orders realised in period k for period t. 
	 * @param t the period represented by this object, e.g. the position of the
	 * period in an array in a lot sizing problem object
	 * @param k the period in which 
	 * @return the convoluted realised orders
	 * @throws ConvolutionNotDefinedException 
	 */
	public RealDistribution realisedDemand(int t, int k) throws ConvolutionNotDefinedException{
		//check if k is lower or equal than t-l_max, then all orders will be open
		if (k <= t-getMaxOrderLeadTime()) return new RealSinglePointDistribution(0.0);
		
		//check if k is bigger than t, then all orders will be realised
		if (k > t) return totalDemand();
		
		//in all other cases, get the corresponding sub array and fold it
		int numberOfRealizedOrders = k-t+getMaxOrderLeadTime(); 
		RealDistribution[] orderDistributions = getOrderDistributions();
		RealDistribution[] ordersToConvolute = new RealDistribution[numberOfRealizedOrders];
		for (int i = 0; i < numberOfRealizedOrders; i++){
			ordersToConvolute[i] = orderDistributions[getMaxOrderLeadTime()-i];
		}
		return Convoluter.convolute(ordersToConvolute);
	}

	/**
	 * 
	 * @param t the period represented by this object, e.g. the position of the
	 * period in an array in a lot sizing problem object
	 * @param k the period in which the demand is observed
	 * @return the convoluted open orders
	 * @throws ConvolutionNotDefinedException 
	 */
	public RealDistribution openDemand(int t, int k) throws ConvolutionNotDefinedException{
		//check if k is lower or equal than t-l_max, then all orders will be open
		if (k <= t-getMaxOrderLeadTime()) return totalDemand();
		
		//check if k is bigger than t, then all orders will be realised
		if (k > t) return new RealSinglePointDistribution(0.0);
		
		//in all other cases, get the corresponding sub array and fold it
		int numberOfOpenOrders = t-k+1; 
		RealDistribution[] orderDistributions = getOrderDistributions();
		RealDistribution[] ordersToConvolute = new RealDistribution[numberOfOpenOrders];
		for (int i = 0; i < numberOfOpenOrders; i++){
			ordersToConvolute[i] = orderDistributions[i];
		}
		return Convoluter.convolute(ordersToConvolute);
	}
}
