/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.util;

import de.kuei.scm.lotsizing.dynamic.stochastic.solver.SolvingInitialisiationException;

/**
 * A class which can represent a setup pattern but mainly contains static utility functions
 * for setup pattern
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

	public static void printlnPattern(boolean[] pattern){
		System.out.print("|");
		for (int i = 0; i < pattern.length; i++){
			if (pattern[i]) System.out.print(""+1);
			else System.out.print(""+0);
			System.out.print("|");
		}
		System.out.println("");
	}
	
	public static void printPattern(boolean[] pattern){
		System.out.print("|");
		for (int i = 0; i < pattern.length; i++){
			if (pattern[i]) System.out.print(""+1);
			else System.out.print(""+0);
			System.out.print("|");
		}
	}
	
	
	
	
	
	
}
