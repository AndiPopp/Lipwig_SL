package de.kuei.scm.distribution;

/**
 * @author Andi Popp
 * An exception thrown a convolution is attempted, but the convolution
 * is not easily calcuable
 */
public class ConvolutionNotDefinedException extends RuntimeException {

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
