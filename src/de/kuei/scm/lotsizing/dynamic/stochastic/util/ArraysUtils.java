/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.util;

/**
 * Just some simple utils for arrays
 * @author Andi Popp
 *
 */
public class ArraysUtils {

	/**
	 * Compares the two boolean arrays deeply
	 * @param array1
	 * @param array2
	 * @return false if arrays have different length or different values at a specific index,
	 * true otherwise
	 */
	public static boolean deepEquals(boolean[] array1, boolean[] array2){
		if (array1.length != array2.length) return false;
		for (int i = 0; i < array1.length; i++){
			if (!(array1[i] == array2[i])) return false;
		}
		return true;
	}
	
}
