package io.pivotal.services.dataTx.geode.health.api.reporting;

/**
 * @author Gregory Green
 */
public interface ReportingConstants {

    /**
     * JVM_MAX_MEMORY_CHART_NM = "jvmMaxMemoryChart"
     */
    public static final String JVM_MAX_MEMORY_CHART_NM = "jvmMaxMemoryChart";

    /**
     * JVM_AVG_MEMORY_CHART_NM = "jvmAvgMemoryChart"
     */
    public static final String JVM_AVG_MEMORY_CHART_NM = "jvmAvgMemoryChart";

    /**
     * PAR_NEW_COLLECTIONS_CHART_NM = "parNewCollectionsChart"
     */
    public static final String PAR_NEW_COLLECTIONS_CHART_NM = "parNewCollectionsChart";


    /**
     * PAR_NEW_COLLECTION_TIME_CHART_NM = "parNewCollectionTimeChart"
     */
    public static final String PAR_NEW_COLLECTION_TIME_CHART_NM = "parNewCollectionTimeChart";

    /**
     * CPU_USAGE_CHART_NM = "cpuUsage"
     */
    public static final String CPU_USAGE_CHART_NM = "cpuActives";


    /**
     * IOWAITS_USAGE_CHART_NM = "ioWait"
     */
    public static final String IOWAITS_USAGE_CHART_NM = "ioWait";

    /**
     * STAT_SAMPLER_DELAY_DURATION = "statSamplerDelayDuration"
     */
    public static final String STAT_SAMPLER_DELAY_DURATION_CHART_NM = "statSamplerDelayDuration";
}
