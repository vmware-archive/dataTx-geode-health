package io.pivotal.services.dataTx.gART.api.reporting.vistors;

import io.pivotal.services.dataTx.geode.office.AbstractChartVisitor;
import io.pivotal.services.dataTx.geode.operations.stats.ResourceInst;
import io.pivotal.services.dataTx.geode.operations.stats.ResourceType;
import io.pivotal.services.dataTx.geode.operations.stats.StatDescriptor;
import io.pivotal.services.dataTx.geode.operations.stats.StatValue;
import nyla.solutions.core.data.Property;
import nyla.solutions.core.data.clock.Day;
import nyla.solutions.core.util.Text;
import nyla.solutions.office.chart.Chart;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gregory Green
 */
public class IoWaitStatVisitor extends AbstractChartVisitor
{
    private static final String filterTypeName =  "LinuxSystemStats".toUpperCase();
    private String filterStatName = "iowait";
    private final Day dayFilter;
    private final double ioWaitThreshold;
    private final Map<Property,Double> maxMap = new HashMap<>();

    public IoWaitStatVisitor(Day dayFilter)
    {
        this(10,dayFilter);
    }
    public IoWaitStatVisitor(int ioWaitThreshold,Day dayFilter)
    {
        this.ioWaitThreshold = ioWaitThreshold;
        this.dayFilter = dayFilter;
        this.chart.setGraphType(Chart.BAR_GRAPH_TYPE);

        super.getChart().setTitle("ioWaitThreshold beyond threshold:"+ioWaitThreshold);

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

        for (StatValue statValue : statValues)
        {
            String statName = statValue.getDescriptor().getName();

            if(filterStatName != null && !filterStatName.equalsIgnoreCase(statName))
            {
                continue;  //skip;
            }

            StatValue dataStoreEntryCount = resourceInst.getStatValue(statName);

            if(dataStoreEntryCount.getSnapshotsMaximum() < ioWaitThreshold)
                return;

            StatDescriptor statDescriptor = resourceInst.getType().getStat(statName);
            System.out.println("name:"+name+" resourceType"+resourceType.getName()+".statName:"+statName+" describe:"+statDescriptor.getDescription());

            long [] times = dataStoreEntryCount.getRawAbsoluteTimeStamps();
            double [] values = dataStoreEntryCount.getSnapshots();


            String timeFormat = "HH:mm:ss";


            for (int i = 0; i < values.length; i++)
            {
                Date date = new Date(times[i]);

                Day day = new Day(date);
                if(!this.dayFilter.isSameDay(day))
                    continue;

                String timeValueText = Text.formatDate(timeFormat,date);
                Property timeValue = new Property(name,timeValueText);

                //get previous Max
                Double max = this.maxMap.get(timeValue);

                if(max == null)
                    max = values[i];
                else
                    max = Double.valueOf(Math.max(values[i], max.doubleValue()));

                this.maxMap.put(timeValue, max);

                if(values[i] >= ioWaitThreshold)
                    this.chart.plotValue(max,machine, (String)timeValue.getValue());
            }

        }

    }//------------------------------------------------
}
