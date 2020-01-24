Feature: Analysis log files
[Description] 
Check error, warn and severe.

Background:

  Given log files:   
  |/Projects/Transportation/Penske/docs/analyze/outage-4-14-2019|

    
Scenario Outline: Check for logs with warn, error, and severe levels 
Then Check for logs with level <loglevel> 

 Examples:
    | loglevel  |
    | severe | 
		| warn |
		| error |		     