package de.kuei.scm.distribution;

/**
 * An exception thrown a convolution is attempted, but the convolution
 * is not easily calcuable
 * @author Andi Popp
 *
 */
public class ConvolutionNotDefinedException extends Exception {

	 /**
	 * 
	 */
	private static final long serialVersionUID = -5358655275555899105L;
	
	public ConvolutionNotDefinedException()
	  {
		super();	
	  }
	  public ConvolutionNotDefinedException(String s)
	  {
	    super(s);	
	  }
	
}
