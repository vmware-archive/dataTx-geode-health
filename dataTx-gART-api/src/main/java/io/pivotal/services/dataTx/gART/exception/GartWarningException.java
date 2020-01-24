package io.pivotal.services.dataTx.gART.exception;


public class GartWarningException extends GartException
{
	/**
	 * CATEGORY  = "WARN"
	 */
	public static final String CATEGORY  = "WARN";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1979975081019346666L;
	

	public GartWarningException(String code, String message, Throwable cause)
	{
		super(code,CATEGORY, message ,cause );
	}

	public GartWarningException(String code, String message)
	{
		super(code,CATEGORY,message);
	}
}
