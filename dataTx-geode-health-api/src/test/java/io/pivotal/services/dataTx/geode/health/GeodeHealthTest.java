package io.pivotal.services.dataTx.geode.health;

import io.pivotal.services.dataTx.geode.health.exception.GeodeHealthException;
import nyla.solutions.core.exception.fault.Fault;
import nyla.solutions.core.exception.fault.FaultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class GeodeHealthTest
{
	
	@BeforeEach
	public void initGart()
	{
		File file = new File("src/test/resources/cache-xmls/server1.xml");
		GeodeHealth.getInstance().constructServerXml(Collections.singleton(file.getPath()));
	}


	@Test
	public void testConstructServerXml()
	throws Exception
	{
		assertNotNull(GeodeHealth.getInstance());
	}

	@Test
	public void testAssertGwSenderBatchConflationEnabled()
	{
		GeodeHealth.getInstance().assertGwSenderBatchConflationEnabled();
	}

	@Test
	public void testAssertGwSenderHasSocketBuffetSize()
	{
		GeodeHealth.getInstance().assertGwSenderHasSocketBuffetSize();
	}


	@Test
	public void testAssertPartitionedRegionParallelGwSender()
	{
		try
		{			
			GeodeHealth.getInstance().assertPartitionedRegionParallelGwSender();
			
			fail("Expected failure");
		}
		catch(GeodeHealthException e)
		{}
	}

	@Test
	public void testAssertDisableAutoDiskCompaction()
	{
		try
		{
			GeodeHealth.getInstance().assertDisableAutoDiskCompaction();
			fail("Expected failure");
		}
		catch(GeodeHealthException e)
		{}
	}

	@Test
	public void testAssertEnableAllowForceCompaction()
	{
		try
		{
			GeodeHealth.getInstance().assertEnableAllowForceCompaction();
			fail("Expected failure");
		}
		catch(GeodeHealthException e)
		{}
	}

	@Test
	public void testAssertRegionStatisticsEnabled()
	{
		try
		{
			GeodeHealth.getInstance().assertRegionStatisticsEnabled();
			fail("Expected failure");
		}
		catch(GeodeHealthException e)
		{}
	}

	@Test
	public void testAssertCacheServerHasSocketBuffetSize()
	{
		try
		{
			GeodeHealth.getInstance().assertCacheServerHasSocketBuffetSize();
		
		fail("Expected failure");
		}
		catch(GeodeHealthException e)
		{}
	}

	@Test
	public void testGetInstance()
	{
		assertNotNull(GeodeHealth.getInstance());
	}

	@Test
	public void testGetGartJmxAsserter()
	{
		assertNull(GeodeHealth.getInstance().getGartJmxAsserter());
	}


	@Test
	public void testAddFault()
	{
		Fault f = new FaultException();
		
		GeodeHealth.getInstance().clearFaults();
		GeodeHealth.getInstance().addFault(null);
		assertNull(GeodeHealth.getInstance().getFaults());
		
		GeodeHealth.getInstance().addFault(f);
		
		assertNotNull(GeodeHealth.getInstance().getFaults());
		
	}

	@Test
	public void testGetFaults()
	{
		Fault f = new FaultException();
		
		GeodeHealth.getInstance().addFault(null);
		assertNull(GeodeHealth.getInstance().getFaults());
		
		GeodeHealth.getInstance().addFault(f);
		
		assertNotNull(GeodeHealth.getInstance().getFaults());
	}

	@Test
	public void testSendNotifications()
	{
		GeodeHealth.getInstance().clearFaults();
		GeodeHealth.getInstance().sendNotifications();
	}

	@Test
	public void testSetNotificationEmails()
	{
		GeodeHealth.getInstance().setNotificationEmails("null");
	}

}
