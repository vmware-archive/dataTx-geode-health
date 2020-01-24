Feature: Security Vulnerabilities
	
	Scenario: GemFire security
	
	Given GemFire Distribute System Member
	Then Assert not empty property security-client-accessor
	And Assert not empty property security-client-accessor-pp
	And Assert not empty property security-client-auth-init
	And Assert not empty property security-client-authenticator
	And Assert not empty property security-client-dhalgo
	And Assert not empty property security-log-level
	And Assert not empty property security-peer-auth-init
	And Assert not empty property security-peer-authenticator
	And Assert not empty property security-peer-verifymember-timeout