package io.pivotal.services.dataTx.geode.health.api.reporting;

import io.pivotal.services.dataTx.geode.health.api.reporting.domain.ReportingSetting;
import io.pivotal.services.dataTx.geode.health.api.reporting.vistors.GenericStatVisitor;
import io.pivotal.services.dataTx.geode.health.api.reporting.vistors.IoWaitStatVisitor;
import io.pivotal.services.dataTx.geode.health.api.reporting.vistors.StatVisitorBuilder;
import io.pivotal.services.dataTx.geode.office.*;
import nyla.solutions.core.data.Textable;
import nyla.solutions.core.data.clock.Day;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.decorator.MappedTextFormatDecorator;
import nyla.solutions.core.patterns.decorator.StringText;
import nyla.solutions.core.util.Text;
import nyla.solutions.office.chart.Chart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Builder pattern for the Geode reports
 * @author Gregory Green
 */
public class GeodeReportBuilder
{

	private final File statsFileOrDirector;
	private final File jvmAvgFile;
	private final File jvmMaxFile;
	private final File cpuUsageFile;
	private final File outputDirectory;
	private final String chartTemplate;
	private final File parNewCollectionTimeFile;
	private final File parNewCollectionsFile;
	private final File ioWaitFile;
	private final String template;
	private final File statSamplerDelayDurationFile;

	private Map<String,Textable> textableMap = new HashMap<>();
	private MappedTextFormatDecorator decorator = new MappedTextFormatDecorator();


	/**
	 *
	 * @param statsFileOrDirector the statis file or director
	 * @param outputDirectory the output directory of the report
	 * @param template the HTML template
	 * @throws IOException when an IOException occurs
	 */
	public GeodeReportBuilder(File statsFileOrDirector, File outputDirectory, String template)
	throws IOException
	{
		this.template = template;
		this.decorator.setTemplate(template);

		this.statsFileOrDirector = statsFileOrDirector;

		this.outputDirectory = outputDirectory;

		if(!this.outputDirectory.exists())
		{
			if(!this.outputDirectory.mkdirs())
				throw new SystemException("Unable to create output directory "+outputDirectory);
		}

		this.jvmAvgFile = Paths.get(outputDirectory.getAbsolutePath()+"/"+"jvmAvg.png").toFile();

		this. jvmMaxFile = Paths.get(outputDirectory.getAbsolutePath()+"/"+"jvmMax.png").toFile();
		this.cpuUsageFile = Paths.get(outputDirectory.getAbsolutePath()+"/"+"cpuUsage.png").toFile();
		this.statSamplerDelayDurationFile = Paths.get(outputDirectory.getAbsolutePath()+"/"+"statSamplerDelayDuration.png").toFile();

		this.parNewCollectionTimeFile = Paths.get(outputDirectory.getAbsolutePath()+"/"+"parNewCollectionTime.png").toFile();
		this.parNewCollectionsFile = Paths.get(outputDirectory.getAbsolutePath()+"/"+"parNewCollections.png").toFile();

		this.ioWaitFile = Paths.get(outputDirectory.getAbsolutePath()+"/"+"ioWait.png").toFile();


		chartTemplate = Text.loadTemplate("chart");
	}//------------------------------------------------------

	public void buildJvmAvgHeap(ReportingSetting reportingSetting) {

		try {

			//avgMemoryThreshold
			JvmAvgHeapUsageAboveThresholdChartStatsVisitor v = new JvmAvgHeapUsageAboveThresholdChartStatsVisitor();
			v.setPercentThreshold(reportingSetting.getAvgMemoryThreshold());


			this.writeChart(jvmAvgFile, new StatsToChart
					(v).convert(statsFileOrDirector));

			Textable textable = () -> {
				Map<String, String> map = new HashMap<>();
				map.put("title", "JVM Average Heap Usage Above Threshold Chart");
				map.put("imagePath", outputDirectory.getAbsolutePath() + "/" + jvmAvgFile.getName());

				return Text.format(chartTemplate, map);
			};


			this.textableMap.put(ReportingConstants.JVM_AVG_MEMORY_CHART_NM, textable);
		}
		catch(RequiredException e)
		{
			this.textableMap.put(ReportingConstants.JVM_AVG_MEMORY_CHART_NM, new StringText("Average Memory Below Threshold"));
		}
	}//------------------------------------------------

	public void buildParNewCollections(ReportingSetting reportingSetting)
	{
		writeChart(parNewCollectionsFile, new StatsToChart
				(new ParNewCollectionsChartStatsVisitor(
						reportingSetting.getDayFilter()))
				.convert(statsFileOrDirector));

		Textable textable = () -> {
			Map<String, String> map = new HashMap<>();
			map.put("title", "Par New Collections Above Threshold Chart");
			map.put("imagePath", outputDirectory.getAbsolutePath() + "/" + parNewCollectionsFile.getName());

			return Text.format(this.chartTemplate, map);
		};

		this.textableMap.put(ReportingConstants.PAR_NEW_COLLECTIONS_CHART_NM,textable);

	}//-------------------------------------------

	public void buildParNewCollectionTime(ReportingSetting reportingSetting)
	{
		writeChart(parNewCollectionTimeFile, new StatsToChart
				(new ParNewCollectionTimeThresholdChartStatsVisitor(
						reportingSetting.getDayFilter()))
				.convert(statsFileOrDirector));


		Textable textable = () -> {
			Map<String, String> map = new HashMap<>();
			map.put("title", "Collection Time Above Threshold Chart");
			map.put("imagePath", outputDirectory.getAbsolutePath() + "/" + parNewCollectionTimeFile.getName());

			return Text.format(this.chartTemplate, map);
		};


		this.textableMap.put(ReportingConstants.PAR_NEW_COLLECTION_TIME_CHART_NM,textable);
	}//---------------------------------


	public void buildStatSamplerDelayDuration(ReportingSetting reportingSetting)
	{
		try
		{
			String graphType = Chart.BAR_GRAPH_TYPE;
			String filterTypeName = "StatSampler";
			String filterStatName = "delayDuration";
			double threshold = reportingSetting.getStatSamplerDelayDurationThreshold();
			Day dayFilter = reportingSetting.getDayFilter();

			GenericStatVisitor v = new GenericStatVisitor(
					graphType,
					filterTypeName,
					filterStatName,
					threshold,
					dayFilter);

			writeChart(this.statSamplerDelayDurationFile, new StatsToChart
					(v).convert(statsFileOrDirector));

			Textable textable = () -> {
				Map<String, String> map = new HashMap<>();
				map.put("title", "Stat Sampler Delay Duration Usage Above Threshold Chart :"+reportingSetting.getStatSamplerDelayDurationThreshold());
				map.put("imagePath", outputDirectory.getAbsolutePath() + "/" + this.statSamplerDelayDurationFile.getName());

				return Text.format(this.chartTemplate, map);
			};

			this.textableMap.put(ReportingConstants.STAT_SAMPLER_DELAY_DURATION_CHART_NM, textable);

		}
		catch(RequiredException e)
		{
			this.textableMap.put(ReportingConstants.STAT_SAMPLER_DELAY_DURATION_CHART_NM,
					new StringText("StatSampler delayDuration Above Threshold:"+reportingSetting.getStatSamplerDelayDurationThreshold()));

		}
	}//-------------------------------------------

	public void buildIoWaits(ReportingSetting reportingSetting)
	{
		try
		{


			IoWaitStatVisitor v = new IoWaitStatVisitor(
					reportingSetting.getIoWaitThreshold(),
					reportingSetting.getDayFilter());


			writeChart(this.ioWaitFile, new StatsToChart
					(v).convert(statsFileOrDirector));

			Textable textable = () -> {
				Map<String, String> map = new HashMap<>();
				map.put("title", "IoWaits Usage Above Threshold Chart :"+reportingSetting.getIoWaitThreshold());
				map.put("imagePath", outputDirectory.getAbsolutePath() + "/" + ioWaitFile.getName());

				return Text.format(this.chartTemplate, map);
			};

			this.textableMap.put(ReportingConstants.IOWAITS_USAGE_CHART_NM, textable);
		}
		catch(RequiredException e)
		{
			this.textableMap.put(ReportingConstants.IOWAITS_USAGE_CHART_NM,
					new StringText("IOWAITS Below Threshold:"+reportingSetting.getIoWaitThreshold()));
		}

	}//-------------------------------------------

	/**
	 * Building CPU visitor informaiton
	 */
	public void buildCpuUsage(ReportingSetting reportingSetting)
	{
		this.buildBarTypeStats("LinuxSystemStats",
				"cpuActive",
				reportingSetting.getDayFilter(),
				reportingSetting.getCpuUsageThreshold(),
				"HH:mm");

	}//------------------------------------------
	public void buildJvmMaxHeap(ReportingSetting reportingSetting) {
		try
		{

			JvmMaxHeapUsageAboveThresholdChartStatsVisitor v = new JvmMaxHeapUsageAboveThresholdChartStatsVisitor();
			v.setPercentThreshold(reportingSetting.getMaxMemoryThreshold());

			writeChart(jvmMaxFile, new StatsToChart
					(v).convert(statsFileOrDirector));


			Textable textable = () -> {
				Map<String, String> map = new HashMap<>();
				map.put("title", "JVM Maximum Heap Usage Above Threshold Chart");
				map.put("imagePath", outputDirectory.getAbsolutePath() + "/" + jvmMaxFile.getName());

				return Text.format(this.chartTemplate, map);
			};

			this.textableMap.put(ReportingConstants.JVM_MAX_MEMORY_CHART_NM, textable);
		}
		catch(RequiredException e)
		{
			this.textableMap.put(ReportingConstants.JVM_MAX_MEMORY_CHART_NM, new StringText("Max Memory Below Threshold"));
		}

	}//---------------------------------------------


	public void buildAbandonReadRequest(ReportingSetting setting)
	{
		String templatePropName = "abandonedReadRequests";
		buildCacheServerStats(templatePropName,
				setting.getDayFilter(),
				setting.getAbandonedReadRequestsThreshold());
	}//-------------------------------------------
	public void buildFailedConnectionAttempts(ReportingSetting setting)
	{
		buildCacheServerStats("failedConnectionAttempts",
				setting.getDayFilter(),
				setting.getFailedConnectionAttemptsThreshold());
	}//-------------------------------------------
	public void buildConnectionsTimeOut(ReportingSetting setting)
	{
		buildCacheServerStats("connectionsTimedOut",
				setting.getDayFilter(),
				setting.getConnectionTimeoutsThreshold());
	}//-------------------------------------------
	public void buildCacheServerStats(String statName, Day dayFilter, double threshold)
	{
		final String typeName = "CacheServerStats";
		buildBarTypeStats(typeName,statName,dayFilter, threshold);
	}//-------------------------------------------
	protected void buildBarTypeStats(String typeName, String statName, Day dayFilter, double threshold){
		buildBarTypeStats(typeName,statName,dayFilter, threshold, null);
	}//-------------------------------------------
	protected void buildBarTypeStats(String typeName, String statName, Day dayFilter, double threshold, String timeformat)
	{
		try
		{

			GenericStatVisitor v = new StatVisitorBuilder()
					.barGraph()
					.dayFilter(dayFilter)
					.withType(typeName)
					.statName(statName)
					.threshold(threshold)
					.build();

			if(timeformat != null)
				v.setTimeFormat(timeformat);

			File file = Paths.get(outputDirectory.getAbsolutePath()+"/"+statName+".png").toFile();

			writeChart(file, new StatsToChart
					(v).convert(statsFileOrDirector));


			Textable textable = () -> {
				Map<String, String> map = new HashMap<>();
				map.put("title", statName+" Above Threshold Chart :"+threshold);
				map.put("imagePath", outputDirectory.getAbsolutePath() + "/" + file.getName());

				return Text.format(this.chartTemplate, map);
			};

			this.textableMap.put(statName, textable);
		}
		catch(RequiredException e)
		{
			this.textableMap.put(statName, new StringText(statName+" Below Threshold:"+threshold));
		}
	}//-------------------------------------------
	/**
	 * @return the report
	 */
	public String getReport()
	{
		this.decorator.setMap(textableMap);

		return this.decorator.getText();
	}//-------------------------------------------
	/**
	 * Write a graphical chart to a file system
	 * @param file the file the write
	 * @param chart the chart object
	 */
	private void writeChart(File file, Chart chart)
	{
		try
		{
			IO.writeFile(file, chart.getBytes());
		}
		catch(IOException e)
		{
			throw new SystemException(e.getMessage(),e);
		}
	}//-----------------------------------------

}
