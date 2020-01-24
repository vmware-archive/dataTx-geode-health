package io.pivotal.services.dataTx.geode.health.exception;

public class GeodeHealthErrorException extends GeodeHealthException
{
	/**
	 * CATEGORY = "ERROR"
	 */
	public final static String CATEGORY = "ERROR";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1979975081019346666L;
	


	public GeodeHealthErrorException(String errorCode, String message, Throwable cause)
	{
		super(errorCode,CATEGORY,message,cause);

	}

	public GeodeHealthErrorException(String errorCode, String message)
	{
		super(errorCode,CATEGORY,message);
	}	
}
