package io.pivotal.services.dataTx.gART.api.logfile;

import java.io.File;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.pivotal.services.dataTx.gART.exception.GartErrorException;
import nyla.solutions.core.io.IO;

public class LogFileStep
{
	List<File> logFiles = null;
	@Given("^log files:$")
	public void log_files(List<String> filePaths) throws Throwable {
		
		logFiles = IO.find(filePaths,"*.log");
		
	}

	@Then("^Check for logs with level (.*)$")
	public void check_for_logs_with_level(String loglevel) throws Throwable {
	    
		if(logFiles == null)
			return;
		
	   Map<File,Collection<String>> map = IO.grep(loglevel,logFiles);
	    
	    if(map != null && !map.isEmpty())
	    {
	    	StringBuilder output = new StringBuilder("\n");
	    	
	    	File file = null;
	    	for (Map.Entry<File, Collection<String>> entry : map.entrySet())
			{
	    		file = entry.getKey();
	    		
	    		for (String line : entry.getValue())
				{
	    			output.append(file).append(",").append(loglevel).append(",").append(line).append(IO.NEWLINE);
				}
	    		
			}
	    	throw new GartErrorException(loglevel,output.toString());
	    }
	}
}
