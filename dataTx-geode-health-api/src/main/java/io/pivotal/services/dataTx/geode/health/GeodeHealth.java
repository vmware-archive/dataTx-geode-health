package io.pivotal.services.dataTx.geode.health;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.pivotal.services.dataTx.geode.health.api.cachexml.ServerXmlGeode;
import io.pivotal.services.dataTx.geode.health.api.jmx.GeodeJmxAsserter;
import io.pivotal.services.dataTx.geode.health.exception.GeodeHealthWarningException;
import nyla.solutions.core.exception.fault.Fault;
import nyla.solutions.core.exception.fault.FaultsHtmlTextDecorator;
import nyla.solutions.core.patterns.jmx.JMX;
import nyla.solutions.core.util.Config;
import nyla.solutions.email.Email;

/**
 * <pre>
 * Facade for the GemFire assessment report tool.
 * 
 * 
 * export JMX_USER=admin
 * export JMX_PASSWORD="{cryption}-s s-d"
 * 
 * </pre>
 * @author Gregory Green
 *
 */
public class GeodeHealth implements  Closeable
{
	
	/**
	 * 
	 * @param files the server XML files
	 */
	public void constructServerXml(Collection<String> files)
	{
		if(files == null || files.isEmpty())
			return;
		
		for (String file : files)
		{
			if(serverXmlGartMap.containsKey(file))
				continue;
			
			serverXmlGartMap.put(file, new ServerXmlGeode(new File(file)));
		}
			
	}//------------------------------------------------
	/**
	 * Assert gateway has batch conflation enabled
	 */
	public void assertGwSenderBatchConflationEnabled()
	{
		for(Map.Entry<String, ServerXmlGeode> entry : this.serverXmlGartMap.entrySet())
		{
			Set<String> ids = entry.getValue().getGatewaySenderIds();
			
			if(ids == null)
				continue;
			
			for (String id : ids)
			{
				if(!entry.getValue().isGatewaySenderEnableBatchConflation(id))
					throw new GeodeHealthWarningException("GatewaySenderEnableBatchConflation","Gateway sender id:"+id+" does not have sender enabled batch conflation in file:"+entry.getKey());
			}
		}
	}//------------------------------------------------
	
	public void assertXmlPathContains(String xpath, String containsText)
	{
		for(Map.Entry<String, ServerXmlGeode> entry : this.serverXmlGartMap.entrySet())
		{
			if(!entry.getValue().doesXmlPathContains(xpath,containsText))
			{
				throw new GeodeHealthWarningException("Pdx"," xpath:"+xpath+" does not contain "+containsText+" file:"+entry.getKey());
			}
		}
		
	}
	
	/**
	 * Check whether all gateway senders have the socket buffer size set
	 */
	public void assertGwSenderHasSocketBuffetSize()
	{
		for(Map.Entry<String, ServerXmlGeode> entry : this.serverXmlGartMap.entrySet())
		{
			Set<String> ids = entry.getValue().getGatewaySenderIds();
			
			if(ids == null)
				continue;
			
			for (String id : ids)
			{
				if(!entry.getValue().hasGatewaySenderSocketBucketSize(id))
					throw new GeodeHealthWarningException("GatewaySenderSocketBucketSize","Gateway sender id:"+id+" does not set the socket-bucket-size file:"+entry.getKey());
			}
		}
	}//------------------------------------------------
	public void assertPartitionedRegionParallelGwSender()
	{
		for(Map.Entry<String, ServerXmlGeode> entry : this.serverXmlGartMap.entrySet())
		{
			entry.getValue().assertPartitionedRegionParallelGwSender();
		}
	}//------------------------------------------------
	public void assertDisableAutoDiskCompaction()
	{
		for(Map.Entry<String, ServerXmlGeode> entry : this.serverXmlGartMap.entrySet())
		{
			entry.getValue().assertDisableAutoDiskCompaction();
		}
	}//------------------------------------------------
	public void assertEnableAllowForceCompaction()
	{
		for(Map.Entry<String, ServerXmlGeode> entry : this.serverXmlGartMap.entrySet())
		{
			entry.getValue().assertEnableAllowForceCompaction();
		}
	}//------------------------------------------------
	public void assertRegionStatisticsEnabled()
	{
		for(Map.Entry<String, ServerXmlGeode> entry : this.serverXmlGartMap.entrySet())
		{
			entry.getValue().assertRegionStatisticsEnabled();
		}
	}
	
	public void assertCacheServerHasSocketBuffetSize()
	{
		for(Map.Entry<String, ServerXmlGeode> entry : this.serverXmlGartMap.entrySet())
		{
			if(!entry.getValue().hasCachServerHasSocketBufferSize())
				throw new GeodeHealthWarningException("CacheServerSocketSize","file:"+entry.getKey()+" does not set the cache server socket buffer size");
		}
	}//------------------------------------------------
	
	/**
	 * 
	 * @return singleton instance of the GART object
	 */
	public static GeodeHealth getInstance()
	{
		return gart;
	}//------------------------------------------------

	 
	/**
	 * @return the gartJmxAsserter
	 */
	public GeodeJmxAsserter getGartJmxAsserter()
	{
		return gartJmxAsserter;
	}//------------------------------------------------

	public void setJmxConnection(String host,int port)
	{
		synchronized (GeodeHealth.class)
		{
			String user = Config.getProperty("JMX_USER","");
			char[] passwordArray = Config.getPropertyPassword("JMX_PASSWORD","");
			
			if(jmx != null && gartJmxAsserter  != null)
				return;
			
			jmx = JMX.connect(host, port, user, passwordArray);
			this.gartJmxAsserter = new GeodeJmxAsserter(jmx);
		}

	}//------------------------------------------------
	public void addFault(Fault e)
	{
		if(e == null)
			return;
		
		this.faults.add(e);
	}//------------------------------------------------
	/**
	 * 
	 * @return the collection
	 */
	public Collection<Fault> getFaults()
	{
		if(this.faults == null || this.faults.isEmpty())
			return null;
		
		//return copy 
		
		return new ArrayList<Fault>(this.faults);
	}//------------------------------------------------
	/**
	 * Send fault to distributions and clear
	 */
	public void sendNotifications()
	{
		try
		{
			if(this.faults == null || this.faults.isEmpty())
			{
				System.out.println("No faults found");
				return;
			}
			
			
			System.err.println(faults);
			
			
			if(notificationEmails == null || notificationEmails.trim().length() == 0)
			{
				System.out.println("No notification emails");
				return;
			}
			
			FaultsHtmlTextDecorator decorator = new FaultsHtmlTextDecorator(faults);
			
			String exceptionsHtml = decorator.getText();

			
			String mailFrom= Config.getProperty("EMAIL_USER"); 
			char[] mailFromPassword= Config.getPropertyPassword("EMAIL_PASSWORD");
			String mailHost = Config.getProperty("EMAIL_HOST");
			int mailPort= Config.getPropertyInteger("EMAIL_PORT",25).intValue();
			boolean mailSslEnable= Config.getPropertyBoolean("EMAIL_SSL_ENABLED",true).booleanValue();

			if(email == null)
			{
				 email = new Email();

				  email.setSmtpSslEnable(mailSslEnable);
					
				  email.setMailFromPassword(mailFromPassword);
				  email.setAuthenicationRequired(mailFromPassword != null && mailFromPassword.length > 0);
					
					
					email.setMailHost(mailHost);
					email.setMailPort(mailPort);
					email.setMailFromUser(mailFrom);
					email.setSmtpSslEnable(mailSslEnable);
				
			}
				
			email.sendMail(notificationEmails, notificationSubject, exceptionsHtml);
		}
		finally
		{
			clearFaults();
			
		}
	}//------------------------------------------------
	public void clearFaults()
	{
		//clear previous faults
		faults.clear();
	}//------------------------------------------------

	/**
	 * @param notificationEmails the notificationEmails to set
	 */
	public void setNotificationEmails(String notificationEmails)
	{
		this.notificationEmails = notificationEmails;
	}//------------------------------------------------
	@Override
	public void close() throws IOException
	{
		if(jmx != null)
		{
			this.jmx.close();
		}
		
		this.jmx = null;
		
		
		if(email != null)
		{
			email.dispose();
		}
	}//------------------------------------------------

	private Map<String, ServerXmlGeode> serverXmlGartMap = new ConcurrentHashMap<String, ServerXmlGeode>();
	private GeodeJmxAsserter gartJmxAsserter = null;
	private JMX jmx = null;
	private Email email = null;
	private static GeodeHealth gart = new GeodeHealth();
	private Collection<Fault> faults = new ArrayList<Fault>();
	private String notificationEmails;
	private String notificationSubject = Config.getProperty("EMAIL_SUBJECT","GEMFIRE MONITORING NOTIFICATIONS");
	
	

}
