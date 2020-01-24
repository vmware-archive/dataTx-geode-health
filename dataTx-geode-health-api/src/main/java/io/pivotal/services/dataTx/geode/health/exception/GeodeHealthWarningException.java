package io.pivotal.services.dataTx.geode.health.exception;


public class GeodeHealthWarningException extends GeodeHealthException
{
	/**
	 * CATEGORY  = "WARN"
	 */
	public static final String CATEGORY  = "WARN";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1979975081019346666L;
	

	public GeodeHealthWarningException(String code, String message, Throwable cause)
	{
		super(code,CATEGORY, message ,cause );
	}

	public GeodeHealthWarningException(String code, String message)
	{
		super(code,CATEGORY,message);
	}
}
