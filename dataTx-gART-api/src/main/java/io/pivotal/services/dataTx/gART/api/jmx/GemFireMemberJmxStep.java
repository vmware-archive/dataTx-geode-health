package io.pivotal.services.dataTx.gART.api.jmx;

import cucumber.api.java.en.Then;
import io.pivotal.services.dataTx.gART.GeodeART;

public class GemFireMemberJmxStep
{

	@Then("^Assert GemFire->Member Attribute UseMemory <= (\\d+)% of MaximumHeapSize$")
	public void assert_GemFire_Member_Attribute_UsedMemory_LessThans_MaximumHeapSize(int percentage) throws Throwable 
	{
		GeodeART.getInstance().getGartJmxAsserter().assertMemoryUsedLessThanPercentage(percentage);
	}
	
	
	@Then("^Assert GemFire->Member Attribute CpuUsage < (\\d+)$")
	public void assert_GemFire_Member_Attribute_CpuUsage(int cpuUsageLimit) throws Throwable {
		GeodeART.getInstance().getGartJmxAsserter().assertMemberCpuUsageLessThan(cpuUsageLimit);
	}
	
	
}
