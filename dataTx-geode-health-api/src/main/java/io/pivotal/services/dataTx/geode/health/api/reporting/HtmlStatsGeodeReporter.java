package io.pivotal.services.dataTx.geode.health.api.reporting;

import java.io.File;
import java.io.IOException;

import io.pivotal.services.dataTx.geode.health.api.reporting.domain.ReportingSetting;
import nyla.solutions.core.io.IO;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Generates HTML report using Geode statistic files
 *
 * @author Gregory Green
 */
public class HtmlStatsGeodeReporter implements ReportingConstants
{

	private final ReportingSetting setting;

	/**
	 * The reporter geode constructor
	 * @param setting the reporting settings
	 */
	public HtmlStatsGeodeReporter(ReportingSetting setting)
	{
		if(setting ==null)
			throw new IllegalArgumentException("settings required");

		this.setting = new ReportingSetting(setting);
	}//-------------------------------------------

	/**
	 *
	 * @param statsFileOrDir directory containing sts
	 * @param outputReportFile the output report file
	 * @param template the HTML template
	 * @throws IOException when an unknown error occurs
	 */
	public void report(File statsFileOrDir,File outputReportFile, String template)
	throws IOException
	{

		GeodeReportBuilder b = new GeodeReportBuilder(statsFileOrDir,
				outputReportFile.getParentFile(),template);


		new GeodeReportDirector(setting)
				.construct(b);

		String report = b.getReport();

		IO.writeFile(outputReportFile,report);
	}//-------------------------------------------

}
