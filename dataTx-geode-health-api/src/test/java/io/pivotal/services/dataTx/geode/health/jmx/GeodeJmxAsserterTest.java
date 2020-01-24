package io.pivotal.services.dataTx.geode.health.jmx;



import io.pivotal.services.dataTx.geode.health.api.jmx.GeodeJmxAsserter;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.jmx.JMX;
import nyla.solutions.core.util.Config;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import javax.management.ObjectInstance;

@Disabled
public class GeodeJmxAsserterTest
{
	private static JMX jmx =JMX.connect(Config.getProperty("JMX_HOST"), Config.getPropertyInteger("JMX_PORT").intValue());

	@Test
	public void testAssertMemberAttribute()
	throws Exception
	{
		GeodeJmxAsserter gartJmxAsserter = new GeodeJmxAsserter(jmx);
		
		String attributeName = "FreeMemory";
		
		Function<ObjectInstance,Boolean> f = (ObjectInstance oi) ->  
		{ int i = 0; try { i = Integer.parseInt(jmx.getAttribute(oi.getObjectName(),attributeName).toString()); System.out.println("i:"+i); return i > 50;} 
		catch(Exception e){throw new SystemException("value:"+i+e);} } ;
		
		boolean isWarn = true;
		gartJmxAsserter.assertMemberAttribute("> 50%",f,isWarn);
		
	}
	
	@Test
	public void testassertFreeMemoryGreaterThanPercentage()
	{
		GeodeJmxAsserter gartJmxAsserter = new GeodeJmxAsserter(jmx);
		
		gartJmxAsserter.assertMemoryUsedLessThanPercentage(50);
		
	}

}
