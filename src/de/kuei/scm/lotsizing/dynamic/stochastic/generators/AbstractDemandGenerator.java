package de.kuei.scm.lotsizing.dynamic.stochastic.generators;

import org.apache.commons.math3.distribution.RealDistribution;

public abstract class AbstractDemandGenerator {

	/**
	 * Takes the mean and sd of the aggregated distribution and returns order distributions
	 * according to the pattern defined by the constructor
	 * @param mean the mean of the aggregated distribution
	 * @param sd the sd of the aggregated distribution
	 * @return the order distributions
	 */
	public abstract RealDistribution[] getOrderDistributions(double mean, double sd);

}
