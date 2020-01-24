package io.pivotal.services.dataTx.gART.api.reporting.vistors;

import io.pivotal.services.dataTx.geode.operations.stats.visitors.StatsVisitor;
import nyla.solutions.core.data.clock.Day;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatReportBuilderTest
{
    @Test
    public void test_build(){

        GenericStatVisitor v = new StatVisitorBuilder()
                .withType("type")
                .statName("statName")
                .dayFilter("5/3/2322")
                .threshold(23.23)
                .barGraph().build();


        assertEquals("TYPE",v.getFilterTypeName());
        assertEquals("statName",v.getFilterStatName());
        assertEquals(new Day("5/3/2322"),v.getDayFilter());
        assertEquals(23.23,v.getThreshold(),0);


    }
}