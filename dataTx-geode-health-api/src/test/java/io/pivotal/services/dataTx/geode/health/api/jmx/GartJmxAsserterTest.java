package io.pivotal.services.dataTx.geode.health.api.jmx;

import io.pivotal.services.dataTx.geode.health.exception.GeodeHealthException;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.fail;

public class GartJmxAsserterTest
{

	@Test
	public void testCheckPercentageUsed()
	{
		GeodeJmxAsserter gartJmxAsserter = new GeodeJmxAsserter(null);
		gartJmxAsserter.checkPercentageUsed(40, 100*1024, 30*1024);
		
		try
		{
			gartJmxAsserter.checkPercentageUsed(30, 100*1024, 30*1024);
			fail();
		}
		catch(GeodeHealthException e){}
	
		try
		{
			gartJmxAsserter.checkPercentageUsed(20, 100*1024, 30*1024);
			fail();
		}
		catch(GeodeHealthException e){}
		
	}

}
