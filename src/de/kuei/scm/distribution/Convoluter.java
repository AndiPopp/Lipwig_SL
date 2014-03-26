/**
 * 
 */
package de.kuei.scm.distribution;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

/**
 * A class with static functions to convolute distributions 
 * which convolution is easily calculated
 * @author Andi Popp
 */
public class Convoluter {
	
	/**
	 * Calculates the convolution of two distributions
	 * @param Distribution1 The first distribution to be convoluted
	 * @param Distribution2 The second distribution to be convoluted
	 * @return The convolution of both distribution as a new {@link AbstractRealDistribution}
	 * @throws ConvolutionNotDefinedException If the convolution is not easily calculable
	 */
	public static AbstractRealDistribution convolute(RealDistribution Distribution1, RealDistribution Distribution2) throws ConvolutionNotDefinedException{
		//Folding two normal distributions
		if (Distribution1 instanceof NormalDistribution && Distribution2 instanceof NormalDistribution){
			return new NormalDistribution(
					Distribution1.getNumericalMean()+Distribution2.getNumericalMean(), 
					Math.sqrt(Distribution1.getNumericalVariance()+Distribution2.getNumericalVariance())
			);
		}		
		//if nothing applies throw exception
		throw new ConvolutionNotDefinedException("Convoluting of types "+Distribution1.getClass().getName()+" and "+Distribution2.getClass().getName()+" not implemented, properly not effiently calculable");
	}
	
	public static AbstractRealDistribution convolute(RealDistribution[] Distributions) throws ConvolutionNotDefinedException{
		//Folding normal distributions
		if (Distributions instanceof NormalDistribution[]){
			double mean = 0;
			double variance = 0;
			for (int i = 0; i < Distributions.length; i++){
				mean += Distributions[i].getNumericalMean();
				variance += Distributions[i].getNumericalVariance();
			}
			return new NormalDistribution(mean, Math.sqrt(variance));
		}
		//if nothing applies throw exception
				throw new ConvolutionNotDefinedException("Convoluting of type "+Distributions.getClass().getName()+" not implemented, properly not effiently calculable");
	}
	
}
