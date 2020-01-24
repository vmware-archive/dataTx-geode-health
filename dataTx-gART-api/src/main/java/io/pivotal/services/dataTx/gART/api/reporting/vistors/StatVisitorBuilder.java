package io.pivotal.services.dataTx.gART.api.reporting.vistors;

import com.google.common.cache.CacheBuilder;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import nyla.solutions.core.data.clock.Day;
import nyla.solutions.office.chart.Chart;

/**
 * @author Gregory Green
 */
public class StatVisitorBuilder
{
    private String type;
    private String name;
    private Day dayFilter;
    private String graphType;
    private double threshold;
    public StatVisitorBuilder withType(String type)
    {
        this.type = type;
        return this;
    }


    public StatVisitorBuilder threshold(double threshold){
        this.threshold = threshold;
        return this;
    }

    public StatVisitorBuilder statName(String statName)
    {
        this.name = statName;
        return this;
    }

    public StatVisitorBuilder dayFilter(Day day)
    {
        this.dayFilter = day;
        return this;
    }//-------------------------------------------
    public StatVisitorBuilder dayFilter(String day)
    {
        return this.dayFilter(new Day(day));

    }


    public StatVisitorBuilder barGraph()
    {
        this.graphType = Chart.BAR_GRAPH_TYPE;
        return this;
    }

    public GenericStatVisitor build()
    {
        //String graphType, String filterTypeName,String filterStatName, double threshold, Day dayFilter)
        return new GenericStatVisitor(
                this.graphType,
                this.type,
                this.name,
                this.threshold,
                this.dayFilter);
    }
}
