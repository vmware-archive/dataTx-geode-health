package io.pivotal.services.dataTx.gART.jmx;


import org.junit.Ignore;
import org.junit.Test;

import io.pivotal.services.dataTx.gART.api.jmx.GartJmxAsserter;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.jmx.JMX;
import nyla.solutions.core.util.Config;
import java.util.function.Function;
import javax.management.ObjectInstance;

@Ignore
public class GartJmxAsserterTest
{
	private static JMX jmx =JMX.connect(Config.getProperty("JMX_HOST"), Config.getPropertyInteger("JMX_PORT").intValue());

	@Test
	public void testAssertMemberAttribute()
	throws Exception
	{
		GartJmxAsserter gartJmxAsserter = new GartJmxAsserter(jmx);
		
		String attributeName = "FreeMemory";
		
		Function<ObjectInstance,Boolean> f = (ObjectInstance oi) ->  
		{ int i = 0; try { i = Integer.parseInt(jmx.getAttribute(oi.getObjectName(),attributeName).toString()); System.out.println("i:"+i); return i > 50;} 
		catch(Exception e){throw new SystemException("value:"+i+e);} } ;
		
		boolean isWarn = true;
		gartJmxAsserter.assertMemberAttribute("> 50%",f,isWarn);
		
		//QueryExp queryExp = Mockito.isNull();
	}
	
	@Test
	public void testassertFreeMemoryGreaterThanPercentage()
	{
		GartJmxAsserter gartJmxAsserter = new GartJmxAsserter(jmx);
		
		gartJmxAsserter.assertMemoryUsedLessThanPercentage(50);
		
	}

}
