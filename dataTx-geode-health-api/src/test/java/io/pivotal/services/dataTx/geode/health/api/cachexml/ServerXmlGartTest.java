package io.pivotal.services.dataTx.geode.health.api.cachexml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;




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
		
		ServerXmlGeode serverXml = new ServerXmlGeode(file);
		
		Assertions.assertTrue(serverXml.isGatewaySenderParallel("gw_sender_16_1"));
		
		//socket-buffer-size
		Assertions.assertTrue(serverXml.hasGatewaySenderSocketBucketSize("gw_sender_16_1"));

		Assertions.assertFalse(serverXml.hasGatewaySenderSocketBucketSize("invalid"));
		
	}

}
