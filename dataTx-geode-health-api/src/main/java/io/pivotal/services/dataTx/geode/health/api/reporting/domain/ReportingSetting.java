package io.pivotal.services.dataTx.geode.health.api.reporting.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nyla.solutions.core.data.clock.Day;

import java.io.File;

/**
 * @author Gregory Green
 */
@Data
@ToString
public class ReportingSetting
{
    /**
     * Default constructor
     */
    public ReportingSetting(){
    }

    public ReportingSetting(ReportingSetting other){

        this.statsFileOrDirector= other.statsFileOrDirector;
        this.outputDirectory= other.outputDirectory;
        this. dayFilter= other.dayFilter;
        this.ioWaitThreshold= other.ioWaitThreshold;
        this.avgMemoryThreshold= other.avgMemoryThreshold;
        this.cpuUsageThreshold= other.cpuUsageThreshold;
        this.connectionTimeoutsThreshold= other.connectionTimeoutsThreshold;
        this.failedConnectionAttemptsThreshold= other.failedConnectionAttemptsThreshold;
        this.abandonedReadRequestsThreshold= other.abandonedReadRequestsThreshold;
        this.maxMemoryThreshold = other.maxMemoryThreshold;
    }

    public ReportingSetting(File statsFileOrDirector, File outputDirectory, Day dayFilter, int ioWaitThreshold, int avgMemoryThreshold, int cpuUsageThreshold, int connectionTimeoutsThreshold, int failedConnectionAttemptsThreshold, int abandonedReadRequestsThreshold, int maxMemoryThreshold)
    {
        this.statsFileOrDirector = statsFileOrDirector;
        this.outputDirectory = outputDirectory;
        this.dayFilter = dayFilter;
        this.ioWaitThreshold = ioWaitThreshold;
        this.avgMemoryThreshold = avgMemoryThreshold;
        this.cpuUsageThreshold = cpuUsageThreshold;
        this.connectionTimeoutsThreshold = connectionTimeoutsThreshold;
        this.failedConnectionAttemptsThreshold = failedConnectionAttemptsThreshold;
        this.abandonedReadRequestsThreshold = abandonedReadRequestsThreshold;
        this.maxMemoryThreshold = maxMemoryThreshold;
    }

    @Setter @Getter
    private File statsFileOrDirector;
    @Setter @Getter
    private File outputDirectory;

    @Setter @Getter
    private Day dayFilter;

    @Setter @Getter
    private int ioWaitThreshold = 10;

    @Setter @Getter
    private int avgMemoryThreshold;

    @Setter @Getter
    private int cpuUsageThreshold = 50;

    @Setter @Getter
    private int connectionTimeoutsThreshold = 1;

    @Setter @Getter
    private int failedConnectionAttemptsThreshold = 1;

    @Setter @Getter
    private int abandonedReadRequestsThreshold = 1;

    @Setter @Getter
    private int maxMemoryThreshold;

    @Setter @Getter
    private int statSamplerDelayDurationThreshold = 1000;

}
