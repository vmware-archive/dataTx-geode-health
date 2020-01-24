package io.pivotal.services.dataTx.geode.health.api.reporting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import io.pivotal.services.dataTx.geode.health.api.reporting.domain.ReportingSetting;
import nyla.solutions.core.data.clock.Day;
import nyla.solutions.core.io.IO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing the overall flow of the HTML report generation
 * @author Gregory Green
 */
public class HtmlStatsGeodeReporterTest
{

	@Test
	public void test_report_with_template()
	throws IOException
	{
		ReportingSetting setting = new ReportingSetting();
		setting.setDayFilter(new Day("7/14/2019"));

		String template = IO.readFile("src/test/resources/junit-template/user_report.txt");
		HtmlStatsGeodeReporter reporter = new HtmlStatsGeodeReporter(setting);


		File statsFileOrDir = Paths.get("src/test/resources/stats").toFile();

		File reportFile = new File("target/report_test.html");
		reportFile.delete();

		reporter.report(statsFileOrDir,reportFile,template);
		assertTrue(Paths.get(reportFile.getAbsolutePath()).toFile().exists());

		String html = IO.readFile(reportFile);
		assertTrue(html != null && html.contains("This chart is very importance"));
		
	}



}
