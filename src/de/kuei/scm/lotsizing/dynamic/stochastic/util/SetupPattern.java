/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.util;

import de.kuei.scm.lotsizing.dynamic.stochastic.solver.SolvingInitialisiationException;

/**
 * A class which can represent a setup pattern but mainly contains static utility functions
 * for setup patterns
 * @author Andi Popp
 *
 */
public class SetupPattern {

	/**
	 * The actual setup pattern
	 */
	boolean[] pattern;

	/**
	 * Full parameter constructor
	 * @param pattern
	 */
	public SetupPattern(boolean[] pattern) {
		this.pattern = pattern;
	}

	/**
	 * A method to enumerate all possible setup patterns, which always start with a setup period. 
	 * Interprets the setup pattern as a binary number and increments it by one. 
	 * @param pattern the current pattern in the sequence
	 * @return the next pattern in the sequence
	 * @throws SolvingInitialisiationException if the last pattern has been enumerated
	 */
	public static boolean[] stepPattern(boolean[] pattern) throws SolvingInitialisiationException{
		boolean[] newPattern = new boolean[pattern.length];
		for (int i = 0; i < pattern.length; i++){
			newPattern[i] = pattern[i];
		}
		
		for (int i = 1; i < pattern.length; i++){
			if (pattern[i] == false){
				newPattern[i] = true;
				return newPattern;
			}
			else{
				newPattern[i] = false;
			}
		}
		
		throw new SolvingInitialisiationException();
		
	}

	/**
	 * Prints the pattern in a line followed by a line break
	 * @param pattern
	 */
	public static void printlnPattern(boolean[] pattern){
		System.out.print("|");
		for (int i = 0; i < pattern.length; i++){
			if (pattern[i]) System.out.print(""+1);
			else System.out.print(""+0);
			System.out.print("|");
		}
		System.out.println("");
	}
	
	/**
	 * Prints the pattern in a line
	 * @param pattern
	 */
	public static void printPattern(boolean[] pattern){
		System.out.print("|");
		for (int i = 0; i < pattern.length; i++){
			if (pattern[i]) System.out.print(""+1);
			else System.out.print(""+0);
			System.out.print("|");
		}
	}
	
	/**
	 * Gets the indexes of the setup period for a given setup pattern
	 * @param pattern the setup pattern
	 * @return the indexes of the setup periods
	 */
	public static int[] toSetupPeriods(boolean[] pattern){
		
		int numberOfSetups = 0;
		for (boolean a : pattern){
			if (a) numberOfSetups++;
		}
		int[] setupPeriods = new int[numberOfSetups];
		int pointer = 0;
		for (int t = 0; t < pattern.length; t++){
			if (pattern[t]){
				setupPeriods[pointer] = t;
				pointer++;
			}
		}
		return setupPeriods;
	
	}
	
	
	
	
}
