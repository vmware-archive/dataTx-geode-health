package io.pivotal.services.dataTx.gART.api.jmx;


import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;

import io.pivotal.services.dataTx.gART.exception.GartErrorException;
import io.pivotal.services.dataTx.gART.exception.GartWarningException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.jmx.JMX;
import nyla.solutions.core.util.Text;

public class GartJmxAsserter
{

	private final JMX jmx;
	
	public GartJmxAsserter(JMX jmx)
	{
		this.jmx = jmx;
	}//------------------------------------------------
	
	public void assertMemberCpuUsageLessThan(int limit)
	{		
			assertMemberAttributeLessThan("CpuUsage",limit);
	}//------------------------------------------------
	public void assertSystemLocatorCountGreaterThan(int lowerBound)
	{
		assertSystemAttributeGreaterThanEqualTo("LocatorCount", lowerBound);
	}//------------------------------------------------
	public void assertRegionNumberOfBucketsWithoutRedundancy(int maxNumWithout)
	{
		try
		{
			Collection<ObjectName> results = jmx.searchObjectNames("GemFire:service=Region,name=/*,type=Distributed");
			
			int numBucketsWithoutRedundancy = 0;
			for (ObjectName objectName : results)
			{
				numBucketsWithoutRedundancy = jmx.getAttribute(objectName, "NumBucketsWithoutRedundancy");
				
				if(maxNumWithout < numBucketsWithoutRedundancy )
				{
					throw new GartErrorException("NumberOfBucketsWithoutRedundancy","Expected NumBucketsWithoutRedundancy "+maxNumWithout+" > "+numBucketsWithoutRedundancy+"  for "+objectName);
				}
			}
		}
		catch (InstanceNotFoundException e)
		{
			throw new SystemException(e);
		}
	}//------------------------------------------------
	public void assertCacheServerCountGreaterThan(int minCacheServerCnt)
	{
		try
		{
			ObjectName sysObjectName = new ObjectName("GemFire:service=System,type=Distributed");
			
			int memberCount = jmx.getAttribute(sysObjectName, "MemberCount");
			int locatorCount = jmx.getAttribute(sysObjectName, "LocatorCount"); 
			
			int cacheServerCount = memberCount - locatorCount;
			
			if(cacheServerCount < minCacheServerCnt)
				throw new GartErrorException("CacheServerCount","cacheServerCount:"+cacheServerCount+" < "+minCacheServerCnt);
		}
		catch (MalformedObjectNameException | InstanceNotFoundException e)
		{
			throw new SystemException(e);
		}
		
	}//------------------------------------------------
	public void assertSystemAttributeGreaterThanEqualTo(String attributeName, int lowerBound)
	{
		try
		{
			ObjectName sysObjectName = new ObjectName("GemFire:service=System,type=Distributed");
			
			int value = jmx.getAttribute(sysObjectName, attributeName);
			
			if(value < lowerBound)
				throw new GartErrorException(attributeName,attributeName+":"+value+" < "+lowerBound);
		}
		catch (MalformedObjectNameException | InstanceNotFoundException e)
		{
			throw new SystemException(e);
		}
	
	}//------------------------------------------------
	
	public void assertTotalOnlyOnDiskLessThenPercentage(int percentageLimit)
	throws Exception
	{
		Collection<ObjectInstance> rows = selectDistributedRegionsObjects();
		
		ObjectName objectName = null;
		for (ObjectInstance objectInstance : rows)
		{
			objectName = objectInstance.getObjectName();
			long totalOnlyOnDisk = jmx.getAttribute(objectName, "TotalEntriesOnlyOnDisk");
			
			long systemRegionEntryCount = jmx.getAttribute(objectInstance.getObjectName(), "SystemRegionEntryCount");
			
			if(systemRegionEntryCount == 0)
				return;
			
			//what % of SystemRegionEntryCount is totalOnlyOnDisk
			
			long onDiskPercentage = (totalOnlyOnDisk/systemRegionEntryCount)*100;
			
			if(onDiskPercentage > percentageLimit)
				throw new GartWarningException("TotalOnlyOnDisk"," total of disk percentage:"+onDiskPercentage+"% > "+percentageLimit+"% totalOnlyOnDisk:"+totalOnlyOnDisk+"  systemRegionEntryCount:"+systemRegionEntryCount+" region:"+objectName);
		}
	}
	public void assertMemberAttributeLessThan(String attributeName, int limit)
	{
		try
		{
			Set<ObjectInstance> rows = selectMembersObjects();
			
			if(rows == null || rows.isEmpty())
				return;
			
			ObjectName objectName = null;
			for (ObjectInstance objectInstance : rows)
			{
				
				objectName = objectInstance.getObjectName();
				
				//get free memory
				double value = Double.parseDouble(jmx.getAttribute(objectName, attributeName).toString());
			
				
				//assert free memory >= (total memory)/2
				if(value >limit)
					throw new GartWarningException(attributeName,attributeName+" "+value+" > "+limit+" for object "+objectName);
			}
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException(e.getMessage(),e);
		}
	}//------------------------------------------------
	private Set<ObjectInstance> selectDistributedRegionsObjects() throws MalformedObjectNameException, IOException
	{
		ObjectName memberObjectNameQuery = new ObjectName("GemFire:service=Region,name=/*,type=Distributed");
		

		QueryExp queryExp = null;
		Set<ObjectInstance> rows = jmx.queryMBeans(memberObjectNameQuery, queryExp);
		return rows;
	}
	private Set<ObjectInstance> selectMembersObjects() throws MalformedObjectNameException, IOException
	{
		ObjectName memberObjectNameQuery = new ObjectName("GemFire:type=Member,member=*");
		

		QueryExp queryExp = null;
		Set<ObjectInstance> rows = jmx.queryMBeans(memberObjectNameQuery, queryExp);
		return rows;
	}//------------------------------------------------
	public void assertMemoryUsedLessThanPercentage(int percentage)
	{
		try
		{
			Set<ObjectInstance> rows = selectMembersObjects();
			
			if(rows == null || rows.isEmpty())
				return;
			
			ObjectName objectName = null;
			
			for (ObjectInstance objectInstance : rows)
			{
				
				objectName = objectInstance.getObjectName();
				
				//get free memory
				//int freeMemory = Integer.parseInt(jmx.getAttribute(objectName, "FreeMemory").toString());
				long maxMemory = jmx.getAttribute(objectName, "MaxMemory");
				
				long usedMemory = jmx.getAttribute(objectName, "UsedMemory");
				
				
				//assert free memory >= (total memory)/2
				
				checkPercentageUsed(percentage, maxMemory, usedMemory);
					
			}
			
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException(e.getMessage(),e);
		}
		
	}

	public void checkPercentageUsed(int percentage, long maxMemory, long usedMemory)
	{
		
		double percentageDecimal =  (percentage/100.1);
		double comparePercent = maxMemory*percentageDecimal;
		if(usedMemory > (comparePercent))
		{
		    String usedMemoryText = Text.formatDouble(usedMemory/1024.0,"#,##0.000#");
		    
			String maxMemoryGBText = Text.formatDouble(maxMemory/1024.0,"#,##0.000#");
			
			String error = "UsedMemory:"+usedMemoryText+ " GB > %"+percentage+" of max memory:"+ maxMemoryGBText+" GB";
			
			throw new GartWarningException("MemoryUsed",error);
		}
	}//------------------------------------------------
	public void assertMemberAttribute(String functionDesc,Function<ObjectInstance, Boolean> isTrue,boolean isWarn)
	{
		try
		{
			Set<ObjectInstance> rows = selectMembersObjects();
			
			if(rows == null || rows.isEmpty())
				return;
			

			for (ObjectInstance objectInstance : rows)
			{
				
				if(!isTrue.apply(objectInstance).booleanValue())
				{
					String msg = "Failed "+functionDesc+"  objectName:"+objectInstance.getObjectName();
					
					if(isWarn)
						throw new GartWarningException(functionDesc,msg); 
					else
						throw new GartErrorException(functionDesc,msg); 	
				}
			}
			
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException(e.getMessage(),e);
		}
	}//------------------------------------------------
}
