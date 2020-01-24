package io.pivotal.services.dataTx.geode.health.api.cachexml;

import java.util.List;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.pivotal.services.dataTx.geode.health.GeodeHealth;

/**
 * Assert step for cache server XML(s)
 * @author Gregory Green
 *
 */
public class ServerXmlStep
{

	@Given("^cache xml files:$")
	public void cache_xml_files(List<String> files) throws Throwable
	{
	   GeodeHealth.getInstance().constructServerXml(files);
	}//------------------------------------------------

	@Then("^Assert all partitioned region gateway senders are parallel$")
	public void assert_all_partitioned_region_gateway_senders_are_parallel() throws Throwable
	{
		GeodeHealth.getInstance().assertPartitionedRegionParallelGwSender();
	}

	@Then("^Assert gateway sender enable batch conflation is true$")
	public void assert_gateway_sender_enable_batch_conflation_is_true() throws Throwable
	{
		GeodeHealth.getInstance().assertGwSenderBatchConflationEnabled();
	}//------------------------------------------------

	@Then("^Assert cache server socket buffer size is set$")
	public void assert_cache_server_socket_buffer_size_is_set() throws Throwable
	{
		GeodeHealth.getInstance().assertCacheServerHasSocketBuffetSize();
	}//------------------------------------------------

	@Then("^Assert disk store auto compact is false$")
	public void assert_disk_store_auto_compact_is_false() throws Throwable
	{
		GeodeHealth.getInstance().assertDisableAutoDiskCompaction();
	}

	@Then("^Assert disk store allow force compaction is true$")
	public void assert_disk_store_allow_force_compaction_is_true() throws Throwable
	{
		GeodeHealth.getInstance().assertEnableAllowForceCompaction();
	}

	@Then("^Assert region attribute statistics enabled is true$")
	public void assert_region_attribute_statistics_enabled_is_true() throws Throwable
	{
		GeodeHealth.getInstance().assertRegionStatisticsEnabled();
	}
	
	@Then("^Assert partitioned region total number of buckets not less than default (\\d+)$")
	public void assert_partitioned_region_total_number_of_buckets_not_less_than_default(int size) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}
	
	@Then("^Assert gateway sender socket buffer size is set$")
	public void assert_gateway_sender_socket_buffer_size_is_set() throws Throwable {
		GeodeHealth.getInstance().assertGwSenderHasSocketBuffetSize();
		
	}

	@Then("^Assert XML element \"([^\"]*)\" contains \"([^\"]*)\"$")
	public void assert_XML_element_contains(String xpath, String containsText) throws Exception {
		GeodeHealth.getInstance().assertXmlPathContains(xpath,containsText);
	}

}
