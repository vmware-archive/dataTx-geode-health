package io.pivotal.services.dataTx.geode.health.api.jmx;

import cucumber.api.java.en.Then;
import io.pivotal.services.dataTx.geode.health.GeodeHealth;

public class GeodeSystemStep
{

	@Then("^Assert System CacheServer Count > (\\d+)$")
	public void assert_System_CacheServer_Count(int minCacheServerCount) throws Throwable {
		GeodeHealth.getInstance().getGartJmxAsserter().assertCacheServerCountGreaterThan(minCacheServerCount);
	}
	
	@Then("^Assert System LocatorCount  >= (\\d+)$")
	public void assert_System_LocatorCount(int minLocatorCount) throws Throwable {
		GeodeHealth.getInstance().getGartJmxAsserter().assertSystemLocatorCountGreaterThan(minLocatorCount);
	}

}
