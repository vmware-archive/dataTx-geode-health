#Author: ggreen@pivotal.io
Feature: JMX Monitoring

 Background:
 
 Given JMX connection host:localhost port:1099
 Given Email distribution list: ggreen@pivotal.io
  
Scenario: GemFire Member UseMemory Less Than 80%
   Then Assert GemFire->Member Attribute UseMemory <= 80% of MaximumHeapSize

Scenario: CPU Usage
Then Assert GemFire->Member Attribute CpuUsage < 90
 	
Scenario: Persistence Region Entries Only On Disk less than 10 percent of total cluster
Then Assert Persist Regions TotalEntriesOnlyOnDisk < 5% of SystemRegionEntryCount
   
Scenario: System CacheServer Count Greater Than 
Then Assert System CacheServer Count > 1

Scenario: Number of Buckets Without Redundancy
When Assert Region numberOfBucketsWithoutRedundancy <= 0
 
Scenario: Member Locator Count Greater Than or Equals To 
Then Assert System LocatorCount  >= 2   

Scenario: JMX Attribute: GemFire/Distributed/System/Attributes/JVMPauses	
Then Assert JVMPauses < 3 


Scenario: TotalConnectionsTimedOut Server (Member) counts

Then Assert JMX Attribute: GemFire/Member/CacheServer/../Attributes/TotalConnectionsTimedOut < 5	


Scenario: TotalConnectionsTimedOut Server (Member) counts

Then Assert JMX Attribute: GemFire/Member/CacheServer/../Attributes/TotalConnectionsTimedOut < 5	

