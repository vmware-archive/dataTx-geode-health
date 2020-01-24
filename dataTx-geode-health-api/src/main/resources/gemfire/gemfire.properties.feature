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

@tag1
Scenario: Disable enable-cluster-configuration=true when cache-xml-file set

Then I validate the outcomes

@tag2
Scenario Outline:  Set do not mcast-address=239.192.81.1 if mcast-port=0
Then I want to write a step with <name>


Examples:
    | name  |value | status |
    | name1 |  5   | success|
    | name2 |  7   | Fail   |
  
 Scenario Outline: have time statistics disenabled
 Then Assert Member enable-time-statistics=false 
    
Scenario Outline: Set Enforce Unique Host
Then Set Enforce Unique Host

Scenario Outline: enable-network-partition-detection is set to false
Then Assert enable-network-partition-detection is set to true

Scenario Outline: Run pulse in stand alone mode versus embedded in production 
Then Run pulse in stand alone mode versus embedded in production 
 
Scenario Outline: conserve-sockets=false
Then Assert conserve-sockets=false