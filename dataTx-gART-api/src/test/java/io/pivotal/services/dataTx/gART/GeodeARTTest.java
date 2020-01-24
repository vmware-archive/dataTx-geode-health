package io.pivotal.services.dataTx.gART;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.services.dataTx.gART.GeodeART;
import io.pivotal.services.dataTx.gART.exception.GartException;
import nyla.solutions.core.exception.fault.Fault;
import nyla.solutions.core.exception.fault.FaultException;

public class GeodeARTTest
{
	
	@Before
	public void initGart()
	{
		File file = new File("src/test/resources/cache-xmls/server1.xml");
		GeodeART.getInstance().constructServerXml(Collections.singleton(file.getPath()));
	}


	@Test
	public void testConstructServerXml()
	throws Exception
	{
		assertNotNull(GeodeART.getInstance());
	}

	@Test
	public void testAssertGwSenderBatchConflationEnabled()
	{
		GeodeART.getInstance().assertGwSenderBatchConflationEnabled();
	}

	@Test
	public void testAssertGwSenderHasSocketBuffetSize()
	{
		GeodeART.getInstance().assertGwSenderHasSocketBuffetSize();
	}


	@Test
	public void testAssertPartitionedRegionParallelGwSender()
	{
		try
		{			
			GeodeART.getInstance().assertPartitionedRegionParallelGwSender();
			
			fail();
		}
		catch(GartException e)
		{}
	}

	@Test
	public void testAssertDisableAutoDiskCompaction()
	{
		try
		{
			GeodeART.getInstance().assertDisableAutoDiskCompaction();
			fail();
		}
		catch(GartException e)
		{}
	}

	@Test
	public void testAssertEnableAllowForceCompaction()
	{
		try
		{
			GeodeART.getInstance().assertEnableAllowForceCompaction();
			fail();
		}
		catch(GartException e)
		{}
	}

	@Test
	public void testAssertRegionStatisticsEnabled()
	{
		try
		{
			GeodeART.getInstance().assertRegionStatisticsEnabled();
		fail();
		}
		catch(GartException e)
		{}
	}

	@Test
	public void testAssertCacheServerHasSocketBuffetSize()
	{
		try
		{
			GeodeART.getInstance().assertCacheServerHasSocketBuffetSize();
		
		fail();
		}
		catch(GartException e)
		{}
	}

	@Test
	public void testGetInstance()
	{
		assertNotNull(GeodeART.getInstance());
	}

	@Test
	public void testGetGartJmxAsserter()
	{
		assertNull(GeodeART.getInstance().getGartJmxAsserter());
	}


	@Test
	public void testAddFault()
	{
		Fault f = new FaultException();
		
		GeodeART.getInstance().clearFaults();
		GeodeART.getInstance().addFault(null);
		Assert.assertNull(GeodeART.getInstance().getFaults());
		
		GeodeART.getInstance().addFault(f);
		
		Assert.assertNotNull(GeodeART.getInstance().getFaults());
		
	}

	@Test
	public void testGetFaults()
	{
		Fault f = new FaultException();
		
		GeodeART.getInstance().addFault(null);
		Assert.assertNull(GeodeART.getInstance().getFaults());
		
		GeodeART.getInstance().addFault(f);
		
		Assert.assertNotNull(GeodeART.getInstance().getFaults());
	}

	@Test
	public void testSendNotifications()
	{
		GeodeART.getInstance().clearFaults();
		GeodeART.getInstance().sendNotifications();
	}

	@Test
	public void testSetNotificationEmails()
	{
		GeodeART.getInstance().setNotificationEmails("null");
	}

}
