/**
 * 
 */
package de.kuei.scm.distribution;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/**
 * @author Andi Popp
 *
 */
public class RealSinglePointDistribution extends AbstractRealDistribution {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6626314791852930022L;
	
	/**
	 * The mass point
	 */
	double point;
	
	/**
	 * @param point
	 */
	public RealSinglePointDistribution(double point) {
		this(new Well19937c(),point);
	}
	
	/**
	 * @param point
	 */
	public RealSinglePointDistribution(RandomGenerator rng, double point) {
		super(rng);
		this.point = point;
	}


	/* (non-Javadoc)
	 * @see org.apache.commons.math3.distribution.RealDistribution#density(double)
	 */
	@Override
	public double density(double x) {
		if (x == this.point) return 1.0;
		return 0.0;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.distribution.RealDistribution#cumulativeProbability(double)
	 */
	@Override
	public double cumulativeProbability(double x) {
		if (x >= this.point) return 1.0;
		else return 0.0;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.distribution.RealDistribution#getNumericalMean()
	 */
	@Override
	public double getNumericalMean() {
		return this.point;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.distribution.RealDistribution#getNumericalVariance()
	 */
	@Override
	public double getNumericalVariance() {
		return 0.0;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.distribution.RealDistribution#getSupportLowerBound()
	 */
	@Override
	public double getSupportLowerBound() {
		return this.point;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.distribution.RealDistribution#getSupportUpperBound()
	 */
	@Override
	public double getSupportUpperBound() {
		return this.point;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.distribution.RealDistribution#isSupportLowerBoundInclusive()
	 */
	@Override
	public boolean isSupportLowerBoundInclusive() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.distribution.RealDistribution#isSupportUpperBoundInclusive()
	 */
	@Override
	public boolean isSupportUpperBoundInclusive() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.distribution.RealDistribution#isSupportConnected()
	 */
	@Override
	public boolean isSupportConnected() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.math3.distribution.RealDistribution#sample()
	 */
	@Override
	public double sample(){
		return this.point;
	}
}
