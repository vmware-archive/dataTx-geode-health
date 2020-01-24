package io.pivotal.services.dataTx.geode.health.api.reporting;

import io.pivotal.services.dataTx.geode.health.api.reporting.domain.ReportingSetting;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeodeReportDirectorTest
{
    @Test
    public void test_constructor()
    {
        ReportingSetting settings = new ReportingSetting();
        GeodeReportDirector d = new GeodeReportDirector(settings);

        assertEquals(settings, d.getReportingSetting());
    }
}