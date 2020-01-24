#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios 
#<> (placeholder)
#""
## (Comments)

#Sample Feature Definition Template
@tag
Feature: Title of your feature
	I want to use this template for my feature file

 	Scenario: Use Partitioned Entries Copies
 	Then Partition Region property numberOfCopies > 1
 	
 	
 	
    Scenario:  CPU allocation per number of cache server nodes
    Then Assert a Cache Server #CPU > 4
    
    Scenario: Partitioned Region Bucket Sizing
    Then Assert Partitioned Region List Fixed Partition Attributes totalNumBuckets >= (10 * Cache Server Count)
    
    
    Scenario: Cache Server JVM Heap Sizing
    Then Assert Cache Server MaxMemory <= 64GB
    
    
       Scenario:  Set the redundancy zone
    Then Assert Cache Server Property redundancy-zone not empty
    
