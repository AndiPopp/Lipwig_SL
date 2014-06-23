package de.kuei.scm.lotsizing.dynamic.stochastic.util;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;

public class SimpleSimpsonIntegrator implements UnivariateIntegrator {

	@Override
	public double getRelativeAccuracy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAbsoluteAccuracy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinimalIterationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaximalIterationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double integrate(int N, UnivariateFunction f, double min,
			double max){
		
		double h = (max-min)/N;
		
		double Q = f.value(min)/2;
		
		for (int k = 1; k < N; k++){
			Q += f.value(min+(k*h));
		}
		
		for (int k = 1; k <= N; k++){
			double arg = ((min+((k-1)*h)) + (min+(k*h))) / 2;
			Q += 2*f.value(arg);
		}
		
		Q += f.value(max)/2;
		
		Q = Q*(h/3);
		
		return Q;		
	}

	@Override
	public int getEvaluations() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIterations() {
		// TODO Auto-generated method stub
		return 0;
	}

}
