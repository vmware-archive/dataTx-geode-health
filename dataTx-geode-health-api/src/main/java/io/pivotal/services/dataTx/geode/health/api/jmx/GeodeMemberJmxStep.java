package io.pivotal.services.dataTx.geode.health.api.jmx;

import cucumber.api.java.en.Then;
import io.pivotal.services.dataTx.geode.health.GeodeHealth;

public class GeodeMemberJmxStep
{

	@Then("^Assert GemFire->Member Attribute UseMemory <= (\\d+)% of MaximumHeapSize$")
	public void assert_GemFire_Member_Attribute_UsedMemory_LessThans_MaximumHeapSize(int percentage) throws Throwable 
	{
		GeodeHealth.getInstance().getGartJmxAsserter().assertMemoryUsedLessThanPercentage(percentage);
	}
	
	
	@Then("^Assert GemFire->Member Attribute CpuUsage < (\\d+)$")
	public void assert_GemFire_Member_Attribute_CpuUsage(int cpuUsageLimit) throws Throwable {
		GeodeHealth.getInstance().getGartJmxAsserter().assertMemberCpuUsageLessThan(cpuUsageLimit);
	}
	
	
}
