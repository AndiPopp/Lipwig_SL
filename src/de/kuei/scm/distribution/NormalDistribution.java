/**
 * 
 */
package de.kuei.scm.distribution;

import org.apache.commons.math3.distribution.RealDistribution;

/**
 * @author andi
 *
 */
public class NormalDistribution extends org.apache.commons.math3.distribution.NormalDistribution
	implements NegatableDistribution, NormalLikeDistribution{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7375679289229175579L;

	public NormalDistribution(double mean, double sd){
		super(mean, sd);
	}
	
	public NormalDistribution(org.apache.commons.math3.distribution.NormalDistribution normalDistribution){
		super(normalDistribution.getMean(), normalDistribution.getStandardDeviation());
	}
	
	@Override
	public RealDistribution negate() {
		return new NormalDistribution(-this.getMean(), this.getStandardDeviation());
	}

	
}
