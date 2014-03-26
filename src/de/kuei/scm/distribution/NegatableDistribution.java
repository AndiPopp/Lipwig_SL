/**
 * 
 */
package de.kuei.scm.distribution;

import org.apache.commons.math3.distribution.RealDistribution;

/**
 * @author Andi Popp
 *
 */
public interface NegatableDistribution extends RealDistribution{

	public RealDistribution negate();
	
}
