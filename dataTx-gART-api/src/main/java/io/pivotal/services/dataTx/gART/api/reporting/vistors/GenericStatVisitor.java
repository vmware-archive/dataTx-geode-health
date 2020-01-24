package io.pivotal.services.dataTx.gART.api.reporting.vistors;

import io.pivotal.services.dataTx.geode.office.AbstractChartVisitor;
import io.pivotal.services.dataTx.geode.operations.stats.ResourceInst;
import io.pivotal.services.dataTx.geode.operations.stats.ResourceType;
import io.pivotal.services.dataTx.geode.operations.stats.StatDescriptor;
import io.pivotal.services.dataTx.geode.operations.stats.StatValue;
import nyla.solutions.core.data.Property;
import nyla.solutions.core.data.clock.Day;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.util.Text;
import nyla.solutions.office.chart.Chart;

import java.util.*;

/**
 * Generic visitor for building charts
 * @author Gregory Green
 */
public class GenericStatVisitor extends AbstractChartVisitor
{
    private final String filterTypeName;
    private final String filterStatName;
    private final Day dayFilter;
    private final double threshold;
    private final String graphType;
    private  String timeFormat = "HH:mm:ss";

    /**
     *
     * @param graphType the chart type
     * @param filterTypeName the statistic type
     * @param filterStatName the stats name
     * @param threshold the threshold
     * @param dayFilter the filter
     */
    public GenericStatVisitor(String graphType, String filterTypeName,String filterStatName, double threshold, Day dayFilter)
    {
        this.filterTypeName = filterTypeName.toUpperCase();
        this.filterStatName = filterStatName;
        this.threshold = threshold;
        this.dayFilter = dayFilter;
        this.graphType = graphType;
        this.chart.setGraphType(graphType);

        super.getChart().setTitle("Type:"+filterTypeName+" stat:"+filterStatName+" beyond threshold:"+threshold);

    }//------------------------------------------------

    @Override
    public void visitResourceInst(ResourceInst resourceInst)
    {
        String name = resourceInst.getName();

        String machine = resourceInst.getArchive().getArchiveInfo().getMachine();

        ResourceType resourceType= resourceInst.getType();

        boolean skip =  resourceType == null || resourceType.getName() == null ||
                (this.filterTypeName != null && !resourceType.getName().toUpperCase().contains(this.filterTypeName));

        if(skip)
        {
            return;
        }

        StatValue[] statValues = resourceInst.getStatValues();
        if(statValues == null)
            return;

        Set<Day> uniqueDates = new HashSet<Day>(20);

        boolean found = false;
        for (StatValue statValue : statValues)
        {
            String statName = statValue.getDescriptor().getName();

            if(filterStatName != null && !filterStatName.equalsIgnoreCase(statName))
            {
                continue;  //skip;
            }

            StatValue dataStoreEntryCount = resourceInst.getStatValue(statName);

            if(statValue.getSnapshotsMaximum() < this.threshold)
                return;

            StatDescriptor statDescriptor = resourceInst.getType().getStat(statName);
            System.out.println("name:"+name+" resourceType"+resourceType.getName()+".statName:"+statName+" describe:"+statDescriptor.getDescription());

            long [] times = dataStoreEntryCount.getRawAbsoluteTimeStamps();
            double [] values = dataStoreEntryCount.getSnapshots();

            Map<String,Double> maxMap = new HashMap<>();


            for (int i = 0; i < values.length; i++)
            {
                Date date = new Date(times[i]);

                Day day = new Day(date);
                uniqueDates.add(day);

                if(!this.dayFilter.isSameDay(day))
                    continue;

                String timeValueText = Text.formatDate(timeFormat,date);
                Property timeValue = new Property(name,timeValueText);

                //get previous Max

                if(values[i] >= this.threshold) {

                    found = true;

                    Double max = maxMap.get(timeValueText);

                    if(max == null)
                        maxMap.put(timeValueText,values[i]);
                    else
                    {
                        //check if previous max if greater in for same time
                        max = Math.max(values[i], max);
                        maxMap.put(timeValueText,max);

                    }
                    this.chart.plotValue(max,machine, (String)timeValue.getValue());
                }
            }
        }

        System.out.println("this.dayFilter="+this.dayFilter+" compares dates:"+uniqueDates);

        if(!found)
            throw new RequiredException("Type:"+this.filterTypeName+" state:"+this.filterStatName+"  day:"+this.filterStatName+" threshold:"+threshold);


    }//------------------------------------------------

    public void setTimeFormat(String timeFormat)
    {
        this.timeFormat = timeFormat;
    }

    public String getFilterStatName()
    {
        return this.filterStatName;
    }//-------------------------------------------

    public String getFilterTypeName()
    {
        return filterTypeName;
    }

    public Day getDayFilter()
    {
        return dayFilter;
    }

    public double getThreshold()
    {
        return threshold;
    }

    public String getGraphType()
    {
        return graphType;
    }

    public String getTimeFormat()
    {
        return timeFormat;
    }
}
