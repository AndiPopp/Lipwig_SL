/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.generators;

import de.kuei.scm.distribution.NormalDistribution;
import de.kuei.scm.distribution.NormalLikeDistribution;
import de.kuei.scm.distribution.RealSinglePointDistribution;

/**
 * This constructor divides the orders equally over a fixed number of forerun periods, 
 * with all orders following a normal distributed demand pattern
 * @author Andi Popp
 *
 */
public class EqualDivisionNormalDistributedDemandGenerator extends AbstractNormalLikeDemandGenerator {

	/**
	 * The forerun, 0 means there is no ADI
	 */
	int forerun;
	
	/**
	 * The offset of how early before the demand period orders arrive
	 */
	int offSet;
			
	/**
	 * Full parameter constructor
	 * @param numberOfForerunPeriods
	 * @param offSet
	 */
	public EqualDivisionNormalDistributedDemandGenerator(
			int numberOfForerunPeriods, int offSet) {
		if (numberOfForerunPeriods < 0 || offSet < 0) throw new IllegalArgumentException(); 
		this.forerun = numberOfForerunPeriods;
		this.offSet = offSet;
	}

	/* (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.constructors.AbstractDemandConstructor#getOrderDistributions(double, double)
	 */
	@Override
	public NormalLikeDistribution[] getOrderDistributions(double mean, double sd) {
		double periodMean = mean/(forerun+1);
		double periodSd = Math.sqrt(((sd*sd)/(forerun+1)));
		NormalDistribution orderDistribution = new NormalDistribution(periodMean, periodSd);
		RealSinglePointDistribution emptyOrder = new RealSinglePointDistribution(0);
		
		NormalLikeDistribution[] orderDistributions = new NormalLikeDistribution[forerun+1+offSet];
		for (int i = 0; i < orderDistributions.length; i++){
			if (i < offSet) orderDistributions[i] = emptyOrder;
			else orderDistributions[i] = orderDistribution;
		}
		return orderDistributions;
	}

}
