package io.pivotal.services.dataTx.gART.api.cachexml;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import io.pivotal.services.dataTx.gART.api.cachexml.ServerXmlGart;


/**
 * @author Gregory Green
 *
 */
public class ServerXmlGartTest
{
	@Test
	public void testCacheXmlGart()
	{
		File file = new File("/Projects/Pivotal/dataEng/dev/gemfire-gart/gemfire-gart-api/src/test/resources/cache-xmls/server1.xml");
		
		ServerXmlGart serverXml = new ServerXmlGart(file);
		
		Assert.assertTrue(serverXml.isGatewaySenderParallel("gw_sender_16_1"));
		
		//socket-buffer-size
		Assert.assertTrue(serverXml.hasGatewaySenderSocketBucketSize("gw_sender_16_1"));
		
		Assert.assertFalse(serverXml.hasGatewaySenderSocketBucketSize("invalid"));
		
	}

}
