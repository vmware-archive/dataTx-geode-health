package io.pivotal.services.dataTx.geode.health.exception;

import io.pivotal.services.dataTx.geode.health.GeodeHealth;
import nyla.solutions.core.exception.fault.FaultException;

public abstract class GeodeHealthException extends FaultException
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1979975081019346666L;
	
	public GeodeHealthException(String errorCode, String category)
	{
		this(errorCode,category,null);
	}//------------------------------------------------


	public GeodeHealthException(String errorCode, String category, String message, Throwable cause)
	{
		super(message, cause);
		this.setCode(errorCode);
		this.setCategory(category);

		GeodeHealth.getInstance().addFault(this);
	}

	public GeodeHealthException(String errorCode, String category, String message)
	{
		this(errorCode,category,message,null);
	}//------------------------------------------------

}
