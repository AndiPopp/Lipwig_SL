/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.generators;

import de.kuei.scm.distribution.NormalLikeDistribution;

/**
 * @author Andi Popp
 *
 */
public abstract class AbstractNormalLikeOrdersGenerator extends
		AbstractOrdersGenerator {

	/*
	 * (non-Javadoc)
	 * @see de.kuei.scm.lotsizing.dynamic.stochastic.generators.AbstractDemandGenerator#getOrderDistributions(double, double)
	 */
	@Override
	public abstract NormalLikeDistribution[] getOrderDistributions(double mean, double sd);
}
