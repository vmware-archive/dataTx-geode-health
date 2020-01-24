package io.pivotal.services.dataTx.geode.health.api.reporting.domain;

import nyla.solutions.core.data.clock.Day;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ReportingSettingTest
{
    @Test
    public void test_copy_constructor()
    {
        ReportingSetting prototype = new ReportingSetting();
        prototype.setCpuUsageThreshold(23);
        prototype.setMaxMemoryThreshold(232);
        prototype.setAvgMemoryThreshold(2323);
        prototype.setIoWaitThreshold(545);
        prototype.setDayFilter(new Day("3/3/2322"));
        prototype.setAbandonedReadRequestsThreshold(565);
        prototype.setConnectionTimeoutsThreshold(2323);
        prototype.setOutputDirectory(new File("src"));
        prototype.setStatsFileOrDirector(new File("target"));



        assertEquals(new ReportingSetting(prototype).getCpuUsageThreshold(),prototype.getCpuUsageThreshold());
        assertEquals(new ReportingSetting(prototype).getIoWaitThreshold(),prototype.getIoWaitThreshold());
        assertEquals(new ReportingSetting(prototype).getAbandonedReadRequestsThreshold(),prototype.getAbandonedReadRequestsThreshold());
        assertEquals(new ReportingSetting(prototype).getAvgMemoryThreshold(),prototype.getAvgMemoryThreshold());
        assertEquals(new ReportingSetting(prototype).getConnectionTimeoutsThreshold(),prototype.getConnectionTimeoutsThreshold());
        assertEquals(new ReportingSetting(prototype).getFailedConnectionAttemptsThreshold(),prototype.getFailedConnectionAttemptsThreshold());
        assertEquals(new ReportingSetting(prototype).getMaxMemoryThreshold(),prototype.getMaxMemoryThreshold());
        assertEquals(new ReportingSetting(prototype).getDayFilter(),prototype.getDayFilter());
        assertEquals(new ReportingSetting(prototype).getOutputDirectory(),prototype.getOutputDirectory());
        assertEquals(new ReportingSetting(prototype).getStatsFileOrDirector(),prototype.getStatsFileOrDirector());

    }

    @Test
    public void test_bean()
    {
        ReportingSetting r = new ReportingSetting();


        assertTrue(r.toString().contains("dayFilter"));

        r.setDayFilter(new Day("5/10/2322"));
        System.out.println("reportSetting:"+r.toString());

        assertTrue(r.toString().contains("5/10/2322"));

    }
}