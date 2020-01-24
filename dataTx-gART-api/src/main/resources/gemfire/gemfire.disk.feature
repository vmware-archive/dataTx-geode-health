Feature: Disk File System Management

	Scenario: statistic archive file are set properly
	[Description]
	It is important to enabled statistics properties in each environment.
	Most support tickets will required that statistics files be provided.
	
	When statistic archive file property is not empty
	Then archive-disk-space-limit is > 0
	
	
	Scenario: Log files are set properly
	[Description]
	It is important to not only enable logging,
	but to also assert the files will rolled as needed by setting the limit to 
	be greater than 0.
	This will prevent the logs from filling up the disk store.
	When log level property is not none
	Then log-disk-space-limit > 0
	And log-file-size-limit > 0
	
	Scenario: Disk store are managed correctly
	[Description]
	If may be desirable disable auto compaction of Disk stores.
	The maximum disk usage limits should be set in addition to asserting that backup 
	are being taken.
	
	When Disk Store Used
	Then Assert Auto Compact is false
	And Assert Force Compaction Allowed  is true
	And Assert Disk Usage Critical Percentage < 95
	And TotalBackupCompleted > 1
	
	
