package io.pivotal.services.dataTx.geode.health;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.cli.Main;
import cucumber.api.junit.Cucumber;
import junit.framework.Test;
import junit.framework.TestSuite;


@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/main/resources/gemfire/gemfire.cache.xml.feature",
		"src/main/resources/gemfire/gemfire.logs.feature",
		"src/main/resources/gemfire/gemfire.jmx.monitoring.feature"}, 
plugin = {"pretty", "json:target/cucumber-report.json", 
		"html:target/cucumber-reports.html"},strict =true)
@Ignore
public class GeodeCucumberQA
{
    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GeodeCucumberQA.class );
    }
    
    public static void main(String[] args)
    throws Throwable
	{
    	if(args == null || args.length <  1)
    	{
    		System.err.println("Usage: java -jar gemfire.gart.api.<version> [featureFile|dir] [loopTrueFalse loopDelay]");
    		System.err.println("Example: java -jar gemfire.gart.api-0.0.1-SNAPSHOT.jar gemfire.jmx.monitoring.feature true 10800");
    		
    		System.exit(-1);
    	}
    	String [] cliArgs = {"-g","io.pivotal.gemfire.gart", "-s",args[0]};
    	
    	boolean loop = false;
    	
    	long delaySeconds = 60*5; //every 5 minutes
		if(args.length > 1)
    	{
			loop = Boolean.parseBoolean(args[1]);

    		if(args.length > 2)
    			delaySeconds = Integer.parseInt(args[2]);
    	}
    	 
    	try
		{
			if(loop)
			{
				while(true)
				{
					execute(cliArgs);
					System.out.println("Sleeping for "+delaySeconds+" seconds ");
					Thread.sleep(delaySeconds*1000);
				}
			}
			else
				execute(cliArgs);
		}
		finally
		{
			try { GeodeHealth.getInstance().close(); } catch(Exception e){}
		}
    	
    	System.exit(1);
	}//------------------------------------------------
    private static void execute(String [] cliArgs)
    {
    	try
		{
			 Main.run(cliArgs, Thread.currentThread().getContextClassLoader());
			 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		GeodeHealth.getInstance().sendNotifications();
    }

}
