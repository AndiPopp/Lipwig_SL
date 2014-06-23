/**
 * 
 */
package de.kuei.scm.distribution;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
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
	 * @throws ConvolutionNotDefinedException if the convolution is not easily calculable
	 */
	public static AbstractRealDistribution convolute(RealDistribution Distribution1, RealDistribution Distribution2) throws ConvolutionNotDefinedException{
		//Folding two normal distributions or a normal distribution with a single point distribution
		if ((Distribution1 instanceof NormalDistribution && Distribution2 instanceof NormalDistribution)
				|| (Distribution1 instanceof RealSinglePointDistribution && Distribution2 instanceof NormalDistribution) 
				|| (Distribution1 instanceof NormalDistribution && Distribution2 instanceof RealSinglePointDistribution) 
			){
			return new NormalDistribution(
					Distribution1.getNumericalMean()+Distribution2.getNumericalMean(), 
					Math.sqrt(Distribution1.getNumericalVariance()+Distribution2.getNumericalVariance())
			);
		}
		//Folding two single point distributions
		else if (Distribution1 instanceof RealSinglePointDistribution && Distribution2 instanceof RealSinglePointDistribution){
			return new RealSinglePointDistribution(Distribution1.getNumericalMean()+Distribution2.getNumericalMean());
		}
		//if nothing applies throw exception
		throw new ConvolutionNotDefinedException("Convoluting of types "+Distribution1.getClass().getName()+" and "+Distribution2.getClass().getName()+" not implemented, propably not effiently calculable");
	}
	
	/**
	 * Convolutes a given array of distributions 
	 * @param distributions the array of distributions
	 * @return the convolution of the distribution
	 * @throws ConvolutionNotDefinedException if the convolution is not easily calculable
	 */
	public static AbstractRealDistribution convolute(RealDistribution[] distributions) throws ConvolutionNotDefinedException{
		//Check Distribution types
		boolean allSinglePointDistributions = true;
		boolean allNormalDistributions = true;
		for (int i = 0; i < distributions.length; i++){
			if (!(distributions[i] instanceof RealSinglePointDistribution)) allSinglePointDistributions = false;
			if (!(distributions[i] instanceof NormalDistribution || distributions[i] instanceof RealSinglePointDistribution)) allNormalDistributions = false;
		}
		
		//Folding single point distributions
		if (allSinglePointDistributions){
			double mean = 0;
			for (int i = 0; i < distributions.length; i++){
				mean += distributions[i].getNumericalMean();
			}
			return new RealSinglePointDistribution(mean);
		}
		//Folding normal distributions
		else if (allNormalDistributions){
			double mean = 0;
			double variance = 0;
			for (int i = 0; i < distributions.length; i++){
				mean += distributions[i].getNumericalMean();
				variance += distributions[i].getNumericalVariance();
			}
			return new NormalDistribution(mean, Math.sqrt(variance));
		}
		//if nothing applies throw exception
		throw new ConvolutionNotDefinedException("Convoluting of type "+distributions.getClass().getName()+" not implemented, properly not effiently calculable");
	}
	
	/**
	 * Convolutes a part of a given array of distributions
	 * @param distributions the array of distributions
	 * @param start the first index of the part to be convoluted (inclusive)
	 * @param end the last index of the part to be convoluted (exclusive)
	 * @return the convolution of the distribution
	 * @throws ConvolutionNotDefinedException if the convolution is not easily calculable
	 */
	public static AbstractRealDistribution convolute(RealDistribution[] distributions, int start, int end) throws ConvolutionNotDefinedException{
		return convolute(ArrayUtils.subarray(distributions, start, end));
	}
	
	/**
	 * Convolutes a part of a given array of distributions starting at index 0 (the first index)
	 * @param distributions the array of distributions
	 * @param end the last index of the part to be convoluted (exclusive)
	 * @return the convolution of the distribution
	 * @throws ConvolutionNotDefinedException if the convolution is not easily calculable
	 */
	public static AbstractRealDistribution convolute(RealDistribution[] distributions, int end) throws ConvolutionNotDefinedException{
		return convolute(distributions, 0, end);
	}
}
