package io.pivotal.services.dataTx.gART.exception;

public class GartErrorException extends GartException
{
	/**
	 * CATEGORY = "ERROR"
	 */
	public final static String CATEGORY = "ERROR";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1979975081019346666L;
	


	public GartErrorException(String errorCode, String message, Throwable cause)
	{
		super(errorCode,CATEGORY,message,cause);

	}

	public GartErrorException(String errorCode, String message)
	{
		super(errorCode,CATEGORY,message);
	}	
}
