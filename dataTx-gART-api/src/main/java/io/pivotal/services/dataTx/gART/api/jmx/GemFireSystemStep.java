package io.pivotal.services.dataTx.gART.api.jmx;

import cucumber.api.java.en.Then;
import io.pivotal.services.dataTx.gART.GeodeART;

public class GemFireSystemStep
{

	@Then("^Assert System CacheServer Count > (\\d+)$")
	public void assert_System_CacheServer_Count(int minCacheServerCount) throws Throwable {
		GeodeART.getInstance().getGartJmxAsserter().assertCacheServerCountGreaterThan(minCacheServerCount);
	}
	
	@Then("^Assert System LocatorCount  >= (\\d+)$")
	public void assert_System_LocatorCount(int minLocatorCount) throws Throwable {
		GeodeART.getInstance().getGartJmxAsserter().assertSystemLocatorCountGreaterThan(minLocatorCount);
	}

}
