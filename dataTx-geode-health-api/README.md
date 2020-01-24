# Overview

API GemFire Assessment Reporting Tool (GART) performance monitoring
checks against GemFire cluster using the following 


	#Author: ggreen@pivotal.io
	Feature: JMX Monitoring
	
	 Background:
	
	  Given JMX connection host:host port:1099 user:user and password:password
	  Given Email distribution list:  ggreen@pivotal.io; green.gregory@gmail.com
	  
	   Scenario: GemFire Member FreeMemory More Than 50%
	   Then Assert GemFire->Member Attribute FreeMemory >= 100% of MaximumHeapSize
	
		 Scenario: CPU Usage
		 Then Assert GemFire->Member Attribute CpuUsage < 90
	 	
	 	 Scenario: Persistence Region Entries Only On Disk less than 10 percent of total cluster
	   Then Assert Persist Regions TotalEntriesOnlyOnDisk < 10% of SystemRegionEntryCount
	   
	   Scenario: System CacheServer Count Greater Than 
	   Then Assert System CacheServer Count > 3
	
	   Scenario: Member Locator Count Greater Than or Equals To 
	   Then Assert System LocatorCount  >= 2   
	   
	   	
	 	Scenario: Number of Buckets Without Redundancy
	 	Then Assert Region numberOfBucketsWithoutRedundancy = 0
	 
# Execution

The GemFire assessment  report API is packaged as a uber Jar.
It can be run directly by the following:




## Setup

### Email

GART can be configured to send email notification for monitoring alerts.

Using the following environment variables

| Env Var    | Description    |
| ---------  | -------------- |
| EMAIL_USER | Ex: email@gmail.com        |
| EMAIL_PASSWORD 	| Encrypted email password (see the below)    |
| EMAIL_HOST 		| SMTP host        	|
| EMAIL_PORT 		| SMTP port (default 25)	  |
| EMAIL_SSL_ENABLED | true or false (defaulttrue)|
|EMAIL_SUBJECT | Default GEMFIRE MONITORING NOTIFICATIONS|
| JMX_USER | GemFire JMX user password        |
| JMX_PASSWORD 	| Encrypted JMX user password (see cryption the below)    |


Example:

	
	export EMAIL_HOST=smtp.gmail.com
	export EMAIL_PORT=465
	export EMAIL_SSL_ENABLED=true
	export EMAIL_SUBJECT="Our Geode Cluster monitoring"
	export EMAIL_USER=me@company.com
	export EMAIL_PASSWORD="{cryption}-2 -21 65 -46 -27 95 "


*Email Password Encryption*

Use the following to generate an encrypted password for your email account."

	mvn exec:java -Dexec.mainClass="nyla.solutions.core.util.Cryption" -Dexec.arguments="PUT-PASSWORD_HERE"
	
	
The output starting with the prefix {cryption} is the encrypted password.

Example:

	{cryption}-107 44 -95 38 67 
	
	
**Email Distribution in feature file**
 
 
The destinations of who will receive the email notifications can be set in the cumber feature file with the following example Given statement.
 

	Given Email distribution list: email1@host;email2@host

Each destination email can be separated by ;

You can add this Given statement to the background section of the feature so each test will can be configured with the same email distribution list (see example src/main/resources/gemfire/gemfire.jmx.monitoring.feature).
 
	 Feature: JMX Monitoring
	
	 Background:
	  Given Email distribution list: email1@host;email2@host

### JMX

Many of the monitoring rules establish a JMX connection to the GemFire cluster.

You can add the following Given statement to the background section to establish a JMX GemFire connection. This host and port are the JMX agent hostname/IP and JMX port.

	  Given JMX connection host:hostname port:1099 user:user and password:password


NOTE: user/password currently not supported

## Usage Example

The following will execute the monitoring rules in a single file (src/main/resources/gemfire/gemfire.jmx.monitoring.feature).

	java -jar gemfire.gart.api.<version>.jar [featureFile|dir] [loopTrueFalse loopDelay]

Example	

	java -jar target/gemfire.gart.api.<version> src/main/resources/gemfire/gemfire.jmx.monitoring.feature
	
 
 The following will execute all feature files in a directory (src/main/resources/gemfire).
 
 
	java -jar target/gemfire.gart.api.<version> src/main/resources/gemfire/


**Continuous monitoring** 

To continue monitoring based on a loop a second and third argument to -Dexec.arguments true <sleepInSeconds>


Example loop ever 300 seconds = 5 minutes

	java -jar target/gemfire.gart.api.<version> src/main/resources/gemfire/gemfire.jmx.monitoring.feature true 300"


# Notes


When the email distribution is email is empty:

Example

	Given Email distribution list:  
  
You will get the following message.

	You can implement missing steps with the snippets below:
	
	@Given("^Email distribution list:$")
	public void email_distribution_list() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

