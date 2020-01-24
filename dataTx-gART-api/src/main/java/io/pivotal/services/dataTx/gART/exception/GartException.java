package io.pivotal.services.dataTx.gART.exception;

import io.pivotal.services.dataTx.gART.GeodeART;
import nyla.solutions.core.exception.fault.FaultException;

public abstract class GartException extends FaultException
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1979975081019346666L;
	
	public GartException(String errorCode, String category)
	{
		this(errorCode,category,null);
	}//------------------------------------------------


	public GartException(String errorCode, String category, String message, Throwable cause)
	{
		super(message, cause);
		this.setCode(errorCode);
		this.setCategory(category);

		GeodeART.getInstance().addFault(this);
	}

	public GartException(String errorCode, String category,String message)
	{
		this(errorCode,category,message,null);
	}//------------------------------------------------

}
