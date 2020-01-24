package io.pivotal.services.dataTx.gART.api.jmx;

import static org.junit.Assert.*;

import org.junit.Test;

import io.pivotal.services.dataTx.gART.api.jmx.GartJmxAsserter;
import io.pivotal.services.dataTx.gART.exception.GartException;

public class GartJmxAsserterTest
{

	@Test
	public void testCheckPercentageUsed()
	{
		GartJmxAsserter gartJmxAsserter = new GartJmxAsserter(null);
		gartJmxAsserter.checkPercentageUsed(40, 100*1024, 30*1024);
		
		try
		{
			gartJmxAsserter.checkPercentageUsed(30, 100*1024, 30*1024);
			fail();
		}
		catch(GartException e){}
	
		try
		{
			gartJmxAsserter.checkPercentageUsed(20, 100*1024, 30*1024);
			fail();
		}
		catch(GartException e){}
		
	}

}
