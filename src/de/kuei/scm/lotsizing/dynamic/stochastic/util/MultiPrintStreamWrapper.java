/**
 * 
 */
package de.kuei.scm.lotsizing.dynamic.stochastic.util;

import java.io.PrintStream;

/**
 * Wraps multiple print streams. The methods print and println are overwritten so that
 * the parameters are passed to the respective functions of the member print stream.
 * @author Andi Popp
 *
 */
public class MultiPrintStreamWrapper extends PrintStream {

	

	PrintStream[] printStreams;

	/**
	 * @param out
	 * @param printStreams
	 */
	public MultiPrintStreamWrapper(	PrintStream[] printStreams) {
		super(printStreams[0]);
		this.printStreams = printStreams;
	}

	@Override
	public void close() {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].close();
		}
	}

	@Override
	public void flush() {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].flush();
		};
	}
	
	@Override
	public void print(boolean b) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].print(b);
		}
	}

	@Override
	public void print(char c) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].print(c);
		}
	}

	@Override
	public void print(char[] s) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].print(s);
		}
	}

	@Override
	public void print(double d) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].print(d);
		}
	}

	@Override
	public void print(float f) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].print(f);
		}
	}

	@Override
	public void print(int b) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].print(b);
		}
	}

	@Override
	public void print(long l) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].print(l);
		};
	}

	@Override
	public void print(Object obj) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].print(obj);
		}
	}

	@Override
	public void print(String s) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].print(s);
		}
	}

	@Override
	public void println() {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].println();
		}
	}

	@Override
	public void println(boolean x) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].println(x);
		}
	}

	@Override
	public void println(char x) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].println(x);
		}
	}

	@Override
	public void println(char[] x) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].println(x);
		}
	}

	@Override
	public void println(double x) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].println(x);
		}
	}

	@Override
	public void println(float x) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].println(x);
		}
	}

	@Override
	public void println(int x) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].println(x);
		}
	}

	@Override
	public void println(long x) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].println(x);
		}
	}

	@Override
	public void println(Object x) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].println(x);
		}
	}

	@Override
	public void println(String x) {
		for (int i = 0; i < printStreams.length; i++){
			printStreams[i].println(x);
		}
	}
	
	
	
	
	
}
