package io.pivotal.services.dataTx.gART.api.reporting;

import io.pivotal.services.dataTx.gART.api.reporting.domain.ReportingSetting;
import org.junit.Test;

import static org.junit.Assert.*;

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