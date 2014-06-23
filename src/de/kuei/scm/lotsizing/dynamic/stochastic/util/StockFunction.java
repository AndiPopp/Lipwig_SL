/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.util;



import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.distribution.RealDistribution;


/**
 * Numerically calculates the expected stock after demand of a lot size / initial stock 
 * with a given demand distribution
 * @author Andi Popp
 */
public class StockFunction implements UnivariateFunction {
	
	/**
	 * The demand distribution
	 */
	RealDistribution demand;
	
	/**
	 * Amount of statistical mass that can be neglected for numerical calculations
	 */
	public double accuracy = 0.00000001;
	
	/**
	 * Integrator with which the function shall be calculated
	 */
	public UnivariateIntegrator integrator = new SimpsonIntegrator();
//	public UnivariateIntegrator integrator = new RombergIntegrator();

	
	
	/**
	 * Constructor with specific demand
	 * @param demand
	 * The given demand distribution
	 */
	public StockFunction(RealDistribution demand) {
		this.demand = demand;
	}
	
	/**
	 * Get the function value for the given lot size
	 */
	public double value(double lotSize) {
			return integratedFirstOrderStockFunction(this.demand, lotSize, integrator, accuracy);
	}

	
	/**
	 * Standard stock function for continuous distributions
	 * @param x
	 * @param demand
	 * @param integrator
	 * @return
	 */
	private static double integratedFirstOrderStockFunction(RealDistribution demand, double x, UnivariateIntegrator integrator, double accuracy){
		StockIntegrand Integrand = new StockIntegrand(demand, x);
		
		
		try{
			double min = demand.inverseCumulativeProbability(accuracy);
			double max = x;
			double aux =  integrator.integrate(4096, Integrand, min, max);
			return aux;
		}
		catch(IllegalArgumentException ex){
			if (ex.getMessage().contains("endpoints do not specify an interval")) return 0;
			else throw new RuntimeException(ex.toString());
		}
		catch(Exception ex){
			System.out.println("Debug: "+ex);
			throw new RuntimeException(ex.toString());
		}
	}
}
