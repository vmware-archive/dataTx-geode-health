#Author: ggreen@piviotal.io
Feature:  WAN Gateways
    Scenario: Connected GatewayReceiver
    Given a Gateway Receiver
    Then Assert Connected Status is true
    
    Scenario: Running GatewayReceiver
    Given a Gateway Receivers
    Then Assert Connected Status is true
    And Assert Connected Gateway Senders Count > 0
    And Assert TotalFailedConnectionAttempts = 1

    Scenario: Connected GatewaySenders
    Given a Gateway Sender
    Then Assert Running status is true 
    And Assert  Connected = true
    
    Scenario: Limit Serial Gateway Senders
    Given a Gateway Serial Sender
    Then Number of Gateway Senders < Cache Server Member Count
            
    Scenario: Gateway Sender Event Queue Size
    Given a Gateway Sender 	
    Then Assert EventQueueSize <= 1000
    
    
    
  Scenario: Use Cache Loader With Eviction
    Given a Region
	When Eviction Used
	Then Assert Cache Has Cache Loader
