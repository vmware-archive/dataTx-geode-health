package io.pivotal.services.dataTx.geode.health.api.jmx;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.pivotal.services.dataTx.geode.health.GeodeHealth;

public class GeodeRegionJmxStep
{

	@Then("^Assert Region numberOfBucketsWithoutRedundancy <= (\\d+)$")
	public void assert_Region_numberOfBucketsWithoutRedundancy(int numberOfWithout) throws Throwable 
	{
		GeodeHealth.getInstance().getGartJmxAsserter().assertRegionNumberOfBucketsWithoutRedundancy(numberOfWithout);
	}

	
	@When("^Assert Persist Regions TotalEntriesOnlyOnDisk < (\\d+)% of SystemRegionEntryCount$")
	public void assert_Persist_Regions_TotalEntriesOnlyOnDisk_of_SystemRegionEntryCount(int onlyOnDiskLimitPercentageLimit) throws Throwable {

		GeodeHealth.getInstance().getGartJmxAsserter().assertTotalOnlyOnDiskLessThenPercentage(onlyOnDiskLimitPercentageLimit);
	}
	

}
