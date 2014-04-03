package de.kuei.scm.lotsizing.dynamic.stochastic.util;

import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 * The integrand of a stock function
 * @author Andi Popp
 *
 */
class StockIntegrand implements UnivariateFunction{
	/**
	 * The Demand
	 */
	RealDistribution Demand;
	
	/**
	 * The value of the upper bound
	 */
	double y; 
	
	public StockIntegrand(RealDistribution Demand, double y) {
		this.Demand = Demand;
		this.y = y;
	}

	public double value(double x) {
		if (Double.isNaN(this.Demand.density(x))) System.out.println(
				"NaN Density at "+x+" of "+Demand+" with mean "+Demand.getNumericalMean()+
				" and sd "+Math.sqrt(Demand.getNumericalVariance()));
		return (this.y-x)*this.Demand.density(x);
	}

}
