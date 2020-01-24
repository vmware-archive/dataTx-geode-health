package io.pivotal.services.dataTx.geode.health.api.reporting.vistors;

import nyla.solutions.core.data.clock.Day;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


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