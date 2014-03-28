package de.kuei.scm.lotsizing.dynamic.stochastic.solver;

import java.util.Iterator;
import java.util.Vector;

import de.kuei.scm.lotsizing.dynamic.stochastic.AbstractLotSizingPeriod;

/**
 * This is wrapper class for an {@link AbstractLotSizingPeriod} object, which
 * has members to solve the problem as a dynamic program
 * @author Andi Popp
 *
 */
public class DPBackwardRecursionPeriod {

	/**
	 * The wrapped lot sizing period
	 */
	public final AbstractLotSizingPeriod period;

	/**
	 * All the decision possibilities
	 */
	public final Vector<DPLotSizingDecision> possibleDecisions;
	
	/**
	 * The optimal decision. Starts as null and has to be explicitly filled by the
	 * {@link #solve()} function.
	 */
	public DPLotSizingDecision optimalDecision;
	
	/**
	 * The partial optimal value corresponding to {@link #optimalDecision}. Must also
	 * be set by the {@link #solve()} function.
	 */
	public Double partialOptimalValue;
		
	/**
	 * Constructs a wrapper around the {@link AbstractLotSizingPeriod} period and 
	 * with the given possible decisions
	 * @param period period the {@link AbstractLotSizingPeriod} to be wrapped
	 * @param laterPeriods
	 */
	public DPBackwardRecursionPeriod(AbstractLotSizingPeriod period,
			Vector<DPLotSizingDecision> laterPeriods) {
		this.period = period;
		this.possibleDecisions = laterPeriods;
	}

	/**
	 * Constructs a wrapper around the {@link AbstractLotSizingPeriod} period with
	 * empty possible decisions
	 * @param period the {@link AbstractLotSizingPeriod} to be wrapped
	 */
	public DPBackwardRecursionPeriod(AbstractLotSizingPeriod period) {
		this.period = period;
		this.possibleDecisions = new Vector<DPLotSizingDecision>();
	}
	
	/**
	 * Goes through all the possible decisions and writes the partial optimal one
	 * into {@link #optimalDecision} and the partial optimal value to {@link #partialOptimalValue}
	 * @throws SolvingInitialisiationException if the vector {@link #possibleDecisions} is empty
	 * @throws WrongOptimisationOrderException if the solving is not in backwards recursion order
	 */
	public void solve() throws SolvingInitialisiationException, WrongOptimisationOrderException{
		if (possibleDecisions.isEmpty()) throw new SolvingInitialisiationException("DP decisions have not been initialized in "+this);
		
		Iterator<DPLotSizingDecision> it = possibleDecisions.iterator();
		
		//Start of with the first decision as possible optimal decision
		this.optimalDecision = it.next();
		if (this.optimalDecision.nextSetupPeriod.partialOptimalValue == null) throw new WrongOptimisationOrderException();
		this.partialOptimalValue = this.optimalDecision.cost + this.optimalDecision.nextSetupPeriod.partialOptimalValue;
		
		//Iterate through the rest and see if there is a better decision
		while (it.hasNext()){
			DPLotSizingDecision decision = it.next();
			if (decision.nextSetupPeriod.partialOptimalValue == null) throw new WrongOptimisationOrderException();
			if (this.partialOptimalValue > decision.cost + decision.nextSetupPeriod.partialOptimalValue){
				this.optimalDecision = decision;
				this.partialOptimalValue = decision.cost + decision.nextSetupPeriod.partialOptimalValue;
			};
		}
	}
	
}
