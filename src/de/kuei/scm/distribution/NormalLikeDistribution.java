/**
 * 
 */
package de.kuei.scm.distribution;

import org.apache.commons.math3.distribution.RealDistribution;

/**
 * An interface to group normal distributions and single point distributions
 * @author Andi Popp
 *
 */
public interface NormalLikeDistribution extends RealDistribution{

	
	public double getMean();
	
	public double getStandardDeviation();
}
