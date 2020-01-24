package io.pivotal.services.dataTx.gART.api.cachexml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import io.pivotal.services.dataTx.gART.exception.GartWarningException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Config;
import nyla.solutions.xml.DOM4J;

/**
 * Wrapper for cache server XML tests
 * @author Gregory Green
 *
 */
public class ServerXmlGart
{
	public ServerXmlGart(File file)
	{
		try
		{
			this.file = file;
			//look files
			this.document = DOM4J.toDocument(IO.readFile(file));
		}
		catch (Exception e)
		{
			throw new SystemException("Unable to process file:"+file+" error:"+e.getMessage(),e);
		}
		
	}//------------------------------------------------
	/**
	 * 
	 * @param gatewaySenderID
	 * @return true if given gateway sender is parallel
	 */
	public boolean isGatewaySenderParallel(String gatewaySenderID)
	{
		return "true".equals(String.valueOf(getAttributeBoolById(gatewaySenderID,"parallel", false)));
	}//------------------------------------------------
	@SuppressWarnings("unchecked")
	public void assertPartitionedRegionParallelGwSender()
	{
		//find all partition regions
		List<Element> regions = this.document.selectNodes("//region");
		
		if(regions == null)
			return;

		Attribute refid = null; 
		String regionName = null;
		
		ArrayList<List<String>> violations = new ArrayList<List<String>>();
		
		for (Element region : regions)
		{
			Attribute regionNameAttr = region.attribute("name");
			if(regionNameAttr == null)
				continue;
			
			regionName = regionNameAttr.getText();
			
			refid = region.attribute("refid");
			
			Element regionAttributes = (Element)region.selectSingleNode("region-attributes");
			
			if(regionAttributes == null)
				continue;
			
			if(refid == null || refid.getValue() == null) {
				refid = regionAttributes.attribute("refid");
			}
			
			if(refid == null)
			{
				//check data-policy
				refid = regionAttributes.attribute("data-policy");
			}
			
			if(refid == null)
				continue;
			
			Attribute gwSenderIdAttr = regionAttributes.attribute("gateway-sender-ids");
			if(gwSenderIdAttr == null || gwSenderIdAttr.getValue() == null)
				continue;
			
			String gatewaySenderID = gwSenderIdAttr.getText();
			
			
			if(isRefIdPartitioned(refid))
			{
				if(!this.isGatewaySenderParallel(gatewaySenderID))
				{
					String[] senderRegion = {regionName, gatewaySenderID};
					violations.add(Arrays.asList(senderRegion));
				}
				
			}
		}
		
		if(!violations.isEmpty())
		{
			throw new GartWarningException("PartitionedRegionParallel","Found partitioned regions with not parallel gateway senders:"+violations+" in file:"+file);
		}
	}//------------------------------------------------
	@SuppressWarnings("unchecked")
	public void assertRegionStatisticsEnabled()
	{
			//find all partition regions
			List<Element> regions = this.document.selectNodes("//region");
			
			if(regions == null)
				return;
			
			ArrayList<String> violations = new ArrayList<String>();
			
			String name = null;
			
			for (Element region : regions)
			{
				Attribute nameAttribute = region.attribute("name");
				if(nameAttribute == null)
					continue;
				
				name = nameAttribute.getText();
				
				if(!"true".equalsIgnoreCase(getRegionAttribute(region,"statistics-enabled")))
						violations.add(name);
				
			}
			
			if(!violations.isEmpty())
			{
				throw new GartWarningException("RegionStatisticsEnabled","Statistics Enabled not enabled for the following regions:"+violations+" in file:"+file);
			}
		}//------------------------------------------------
	private String getRegionAttribute(Element region, String attributeName)
	{		
		Element regionAttributes = (Element)region.selectSingleNode("region-attributes");
		if(regionAttributes == null)
			return null;

		Attribute attribute = regionAttributes.attribute(attributeName);
		
		if(attribute == null)
			return null;
		
		return attribute.getText();
	}//------------------------------------------------
	@SuppressWarnings("unchecked")
	public void assertDisableAutoDiskCompaction()
	{
		List<Element> list = this.document.selectNodes("//disk-store");
		
		if(list == null)
			return;
		
		ArrayList<String> violations = new ArrayList<String>();
		
		String name;
		
		for (Element diskStore : list)
		{
			Attribute nameAttribut = diskStore.attribute("name");
			if(nameAttribut == null)
				continue;
			
			name = nameAttribut.getText();
			
			Attribute autoCompact = diskStore.attribute("auto-compact");
			if(autoCompact == null || autoCompact.getText() == null || !"false".equalsIgnoreCase(autoCompact.getText().trim()))
				violations.add(name);
		}
		
		if(!violations.isEmpty())
			throw new GartWarningException("DisableAutoDiskCompaction","Disk store auto compaction is not disabled:"+violations+" in file:"+file);
		
	}//------------------------------------------------
	@SuppressWarnings("unchecked")
	public void assertEnableAllowForceCompaction()
	{
		List<Element> list = this.document.selectNodes("//disk-store");
		
		if(list == null)
			return;
		
		ArrayList<String> violations = new ArrayList<String>();
		
		String name;
		
		for (Element diskStore : list)
		{
			Attribute nameAttribut = diskStore.attribute("name");
			if(nameAttribut == null)
				continue;
			
			name = nameAttribut.getText();
			
			Attribute autoCompact = diskStore.attribute("allow-force-compaction");
			if(autoCompact == null || autoCompact.getText() == null || !"true".equalsIgnoreCase(autoCompact.getText().trim()))
				violations.add(name);
		}
		
		if(!violations.isEmpty())
			throw new GartWarningException("EnableAllowForceCompaction","Disk store allow-force-compaction is not enabled:"+violations+" in file:"+file);
		
	}//------------------------------------------------	
	private boolean isRefIdPartitioned(Attribute refid)
	{
		if(refid == null)
			return false;
		
		String value = refid.getValue();
		if(value == null)
			return false;
		
		return value.toLowerCase().contains("partition");
	}
	/**
	 * 
	 * @param gatewaySenderID the gateway Sender ID
	 * @return true if socket bucket size is set
	 */
	public boolean hasGatewaySenderSocketBucketSize(String gatewaySenderID)
	{
		String socketBucketSize = getAttributeById(gatewaySenderID,"socket-buffer-size");
		return socketBucketSize != null && socketBucketSize.trim().length() > 0;
		
	}//------------------------------------------------
	public boolean hasCachServerHasSocketBufferSize()
	{
		return hasCachServerHasAttribute("socket-buffer-size");
	}//------------------------------------------------
	private boolean hasCachServerHasAttribute(String attributeName)
	{
		Element element = (Element)this.document.selectSingleNode("//cache-server");
		
		if(element == null)
			return false;
		
		Attribute attribute = element.attribute(attributeName);
		
		if(attribute == null)
			return false;
		
		String value = attribute.getValue();
		
		return value != null && value.trim().length() > 0;
	}//------------------------------------------------
	public boolean isGatewaySenderEnableBatchConflation(String gatewaySenderID)
	{
		return "true".equals(String.valueOf(getAttributeBoolById(gatewaySenderID,"enable-batch-conflation", false)));
	}//------------------------------------------------
	
	private boolean getAttributeBoolById(String gatewaySenderID, String attribute, boolean defaultBool)
	{
		String text = getAttributeById(gatewaySenderID,attribute);
		
		if(text == null)
			return defaultBool;
		
		return "true".equalsIgnoreCase(text);
	}//------------------------------------------------
	private String getAttributeById(String id,String attribute)
	{
		Element element = (Element)this.document.selectSingleNode("//*[@id='"+id+"']");
		if(element == null)
			return null;
		
		Attribute parallel = element.attribute(attribute);
		if(parallel == null)
			return null;
		
		return parallel.getText();
	}//------------------------------------------------
	@SuppressWarnings("unchecked")
	public Set<String> getGatewaySenderIds()
	{
		
		List<Element> attributes = this.document.selectNodes("//gateway-sender[@id]");
		
		if(attributes == null || attributes.isEmpty())
			return null;
		
		HashSet<String> ids = new HashSet<String>();
		for (Element attribute : attributes)
		{
			ids.add(attribute.attributeValue("id"));
		}
		
		return ids;
	}//------------------------------------------------
	@SuppressWarnings("unchecked")
	public boolean doesXmlPathContains(String xpath, String containsText)
	{
		if(containsText == null)
			return false;
		
		DOM4J.treeWalk(this.document);
		//xpath =  "//*[name()='pdx-serializer']/*[name()='class-name']";
		//xpath =  "//*pdx-serializer/*[name()='class-name']";
		List<Element> nodes = this.document.selectNodes(xpath);
		
		if(nodes == null || nodes.isEmpty())
			return false;
		
		String text = null;
		for (Element node : nodes)
		{
			text = node.getText();
			if(text == null)
				continue;
			
			if(text.contains(containsText))
				return true;
		}
		
		return false;
	}
	
	public synchronized static ServerXmlGart getInstance()
	{
		if(gart == null)
			gart = new ServerXmlGart(new File(Config.getProperty("gart.file")));
		
		return gart;
	}//------------------------------------------------

	private final File file;
	private final Document document;
	private static ServerXmlGart gart = null;
	
}
