/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.util;

import java.text.NumberFormat;


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
	
	/**
	 * Get a part on an array
	 * @param data The array to get the sub array from
	 * @param startIndex The first index of the subarray
	 * @param endIndex The first index not in the subarray (last index +1)
	 * @return an array with the elements from data in the fields startIndex to endIndex-1
	 */
	public static Object[] subArray(Object[] data, int startIndex, int endIndex){
		Object[] output = new Object[endIndex-startIndex];
		for (int i = startIndex; i < endIndex; i++){
			output[i-startIndex] = data[i];
		}
		return output;
	}

	/**
	 * Get a part on an array
	 * @param data The array to get the sub array from
	 * @param startIndex The first index of the subarray
	 * @param endIndex The first index not in the subarray (last index +1)
	 * @return an array with the elements from data in the fields startIndex to endIndex-1
	 */
	public static boolean[] subArray(boolean[] data, int startIndex, int endIndex){
		boolean[] output = new boolean[endIndex-startIndex];
		for (int i = startIndex; i < endIndex; i++){
			output[i-startIndex] = data[i];
		}
		return output;
	}
	
	/**
	 * Get a part on an array
	 * @param data The array to get the sub array from
	 * @param startIndex The first index of the subarray
	 * @param endIndex The first index not in the subarray (last index +1)
	 * @return an array with the elements from data in the fields startIndex to endIndex-1
	 */
	public static double[] subArray(double[] data, int startIndex, int endIndex){
		double[] output = new double[endIndex-startIndex];
		for (int i = startIndex; i < endIndex; i++){
			output[i-startIndex] = data[i];
		}
		return output;
	}
	
	public static void printlnArray(Object[] arg){
		for (int i = 0; i < arg.length;i++){
			System.out.println(arg[i]);
		}
	}

	public static void printlnArray(int[] arg){
		for (int i = 0; i < arg.length;i++){
			System.out.println(arg[i]);
		}
	}

	public static void printlnArray(double[] arg){
		for (int i = 0; i < arg.length;i++){
			System.out.println(arg[i]);
		}
	}

	public static void printlnArray(boolean[] arg){
		for (int i = 0; i < arg.length;i++){
			System.out.println(arg[i]);
		}
	}
	
	public static void printArray(boolean[] arg){
		System.out.print("[");
		for (int i = 0; i < arg.length;i++){
			System.out.print(arg[i]);
			if (i < arg.length-1) System.out.print(", ");
		}
		System.out.println("]");
	}
	
	public static void printArray(int[] arg){
		System.out.print("[");
		for (int i = 0; i < arg.length;i++){
			System.out.print(arg[i]);
			if (i < arg.length-1) System.out.print(", ");
		}
		System.out.println("]");
	}
	
	public static void printArray(double[] arg){
		System.out.print("[");
		for (int i = 0; i < arg.length;i++){
			System.out.print(arg[i]);
			if (i < arg.length-1) System.out.print(", ");
		}
		System.out.println("]");
	}
	
	public static void printArray(double[] arg, NumberFormat numberFormat){
		System.out.print("[");
		for (int i = 0; i < arg.length;i++){
			System.out.print(numberFormat.format(arg[i]));
			if (i < arg.length-1) System.out.print(", ");
		}
		System.out.println("]");
	}
	
}
