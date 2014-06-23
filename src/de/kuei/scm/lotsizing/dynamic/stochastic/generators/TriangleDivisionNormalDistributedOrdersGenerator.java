/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.generators;

import de.kuei.scm.distribution.NormalDistribution;
import de.kuei.scm.distribution.NormalLikeDistribution;
import de.kuei.scm.distribution.RealSinglePointDistribution;

/**
 * This constructor divides the orders in a triangle shape over a fixed number of forerun periods, 
 * with all orders following a normal distributed demand pattern
 * @author Andi Popp
 *
 */
public class TriangleDivisionNormalDistributedOrdersGenerator extends AbstractNormalLikeOrdersGenerator {

	/**
	 * The number of order periods. 1 means there is no ADI
	 */
	int numberOfOrderPeriods;
	
	/**
	 * The offset of how early before the demand period orders arrive
	 */
	int offSet;
	
	/**
	 * Full parameter constructor
	 * @param numberOfForerunPeriods
	 * @param offSet
	 */
	public TriangleDivisionNormalDistributedOrdersGenerator(
			int numberOfForerunPeriods, int offSet) {
		if (numberOfForerunPeriods < 1 || offSet < 0) throw new IllegalArgumentException(); 
		this.numberOfOrderPeriods = numberOfForerunPeriods;
		this.offSet = offSet;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.constructors.AbstractDemandConstructor#getOrderDistributions(double, double)
	 */
	@Override
	public NormalLikeDistribution[] getOrderDistributions(double mean, double sd) {
		double[] divisors = new double[numberOfOrderPeriods];
		double sum = 0;
		
		
		for (int i = 0; i < numberOfOrderPeriods;i++){
			divisors[i] = i+1;
			sum += i+1;
		}
	
		
		
		RealSinglePointDistribution emptyOrder = new RealSinglePointDistribution(0);
		
		NormalLikeDistribution[] orderDistributions = new NormalLikeDistribution[numberOfOrderPeriods+offSet];
		for (int i = 0; i < orderDistributions.length; i++){
			if (i < offSet) orderDistributions[i] = emptyOrder;
			else {
				double orderMean = (divisors[i-offSet]/(sum*1.0))*mean;
				double orderSd = Math.sqrt(((divisors[i-offSet]/(sum*1.0))*(sd*sd)));
				orderDistributions[i] = new NormalDistribution(orderMean, orderSd);
			}
		}
		return orderDistributions;
	}

}
