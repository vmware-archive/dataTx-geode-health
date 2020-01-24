package io.pivotal.services.dataTx.gART.api.reporting;

import io.pivotal.services.dataTx.gART.api.reporting.domain.ReportingSetting;
import nyla.solutions.core.patterns.creational.BuilderDirector;
import nyla.solutions.dao.reporting.Report;

/**
 * Coordinates build of the report
 *
 * @author Gregory Green
 */
public class GeodeReportDirector implements BuilderDirector<GeodeReportBuilder>
{
	private final ReportingSetting reportingSetting;

	/**
	 * Constructor passing the report settings
	 * @param settings the report settings
	 */
	public GeodeReportDirector(ReportingSetting settings)
	{
		this.reportingSetting = settings;
	}//-------------------------------------------

	@Override
	public void construct(GeodeReportBuilder builder)
	{
		builder.buildJvmAvgHeap(this.reportingSetting);
		builder.buildJvmMaxHeap(this.reportingSetting);
		builder.buildCpuUsage(this.reportingSetting);
		builder.buildParNewCollections(this.reportingSetting);
		builder.buildParNewCollectionTime(this.reportingSetting);
		builder.buildIoWaits(this.reportingSetting);

		builder.buildAbandonReadRequest(this.reportingSetting);
		builder.buildConnectionsTimeOut(this.reportingSetting);
		builder.buildFailedConnectionAttempts(this.reportingSetting);
		builder.buildStatSamplerDelayDuration(this.reportingSetting);

	}

	/**
	 *
	 * @return
	 */
	public ReportingSetting getReportingSetting()
	{
		if(this.reportingSetting == null)
			return null;

		return new ReportingSetting(this.reportingSetting);
	}
}
