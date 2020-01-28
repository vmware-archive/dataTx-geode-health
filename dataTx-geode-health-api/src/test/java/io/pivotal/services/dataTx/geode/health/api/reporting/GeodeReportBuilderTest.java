package io.pivotal.services.dataTx.geode.health.api.reporting;

import io.pivotal.services.dataTx.geode.health.api.reporting.domain.ReportingSetting;
import nyla.solutions.core.data.clock.Day;
import nyla.solutions.core.io.IO;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Testing for the GeodeReportBuilder class
 * @author Gregory Green
 */
public class GeodeReportBuilderTest
{

	@Test
	public void test_reporting()
	throws Exception
	{
		String template = IO.readFile("src/test/resources/junit-template/user_report.txt");

		Day day = new Day("07/14/2019");
		File file = new File("src/test/resources/stats/");
		GeodeReportBuilder b = new GeodeReportBuilder(file,
				Paths.get("target/reporting").toFile()
						,template);
		ReportingSetting reportingSetting = new ReportingSetting();
		reportingSetting.setDayFilter(day);
		reportingSetting.setStatsFileOrDirector(file);



		b.buildJvmAvgHeap(reportingSetting);
		b.buildJvmMaxHeap(reportingSetting);
		b.buildParNewCollections(reportingSetting);
		b.buildParNewCollectionTime(reportingSetting);
		b.buildCpuUsage(reportingSetting);

		b.buildAbandonReadRequest(reportingSetting);
		b.buildConnectionsTimeOut(reportingSetting);
		b.buildFailedConnectionAttempts(reportingSetting);

		String report = b.getReport();
		System.out.println("report"+report);

		assertNotNull(report);

		assertTrue(report.contains("Average"));
		assertTrue(report.contains("Max"));
		assertTrue(report.contains("Collections"));
		assertTrue(report.contains("Collection Time"));
		assertTrue(report.contains("CPU"));

		assertFalse(report.contains("${cpuUsage}"));
		assertFalse(report.contains("abandon"));
		assertFalse(report.contains("connectionsTimeout"));
		assertFalse(report.contains("failedConnection"));


	}


	@Test
	public void test_cpu_active()
	throws Exception
	{
		String template = IO.readFile("src/test/resources/junit-template/user_report.txt");

		Day day = new Day("05/20/2019");
		File file = new File("src/test/resources/stats/datanode_MacBook-Pro-5.gfs");
		GeodeReportBuilder b = new GeodeReportBuilder(file,
				Paths.get("target/reporting").toFile()
				,template);
		ReportingSetting reportingSetting = new ReportingSetting();
		reportingSetting.setDayFilter(day);
		reportingSetting.setStatsFileOrDirector(file);
		reportingSetting.setCpuUsageThreshold(34);


		b.buildCpuUsage(reportingSetting);

		String report = b.getReport();
		System.out.println("\n\nsreport="+report);

		assertFalse(report.contains("${cpuActive}"));


	}


	@Test
	public void test_delay_duration()
	throws Exception
	{

		String template = IO.readFile("src/test/resources/junit-template/user_report.txt");

		Day day = new Day("05/17/2019");
		File file = new File("src/test/resources/stats/datanode_MacBook-Pro-5.gfs");
		GeodeReportBuilder b = new GeodeReportBuilder(file,
				Paths.get("target/reporting").toFile()
				,template);
		ReportingSetting reportingSetting = new ReportingSetting();
		reportingSetting.setDayFilter(day);
		reportingSetting.setStatsFileOrDirector(file);
		reportingSetting.setCpuUsageThreshold(34);
		reportingSetting.setStatSamplerDelayDurationThreshold(1);


		b.buildStatSamplerDelayDuration(reportingSetting);

		String report = b.getReport();
		System.out.println("\n\nsreport="+report);

		assertFalse(report+"contains delay",report.contains("${statSamplerDelayDuration}"));

	}

}
