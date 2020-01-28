package io.pivotal.services.dataTx.geode.health.api.reporting.database;

import io.pivotal.services.dataTx.geode.health.api.dao.StatDao;
import io.pivotal.services.dataTx.geode.health.api.dao.entity.StatEntity;
import io.pivotal.services.dataTx.geode.operations.stats.ResourceInst;
import io.pivotal.services.dataTx.geode.operations.stats.ResourceType;
import io.pivotal.services.dataTx.geode.operations.stats.StatValue;
import io.pivotal.services.dataTx.geode.operations.stats.visitors.StatsVisitor;
import lombok.Builder;
import lombok.NonNull;
import nyla.solutions.core.patterns.workthread.ExecutorBoss;
import nyla.solutions.core.patterns.workthread.MemorizedQueue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Save status data to database
 *
 * @author Gregory Green
 */
@Builder
public class StatsToDatabaseVisitor implements StatsVisitor
{
    @NonNull
    private final StatDao dao;
    @NonNull
    private final LocalDate dayFilter;
    @NonNull
    private final String statTypeName;
    @NonNull
    private final String statName;

    @NonNull
    private Integer batchSize;

    @Override
    public void visitResourceInst(ResourceInst resourceInst)
    {
        String name = resourceInst.getName();

        ResourceType resourceType = resourceInst.getType();

        if (mustSkip(resourceType))
            return;

        String machine = resourceInst.getArchive().getArchiveInfo().getMachine();
        String resourceTypeName = resourceType.getName();

        StatValue[] statValues = resourceInst.getStatValues();
        if (statValues == null)
            return;


        StatValue statValue = resourceInst.getStatValue(this.statName);

        if (statValue == null)
        {
            throw new IllegalArgumentException("Cannot find stat:" + this.statName + " in " + resourceType.getName());
        }


        StatEntity.StatEntityBuilder builder = StatEntity.builder();

        String statName = statValue.getDescriptor().getName();

        long[] times = statValue.getRawAbsoluteTimeStamps();
        double[] values = statValue.getSnapshots();


        ArrayList<StatEntity> statEntities = new ArrayList<>(batchSize);
        for (int i = 0; i < values.length; i++)
        {
            LocalDateTime statTime = Instant.ofEpochMilli(times[i])
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();

            if (!dayFilter.equals(statTime.toLocalDate()))
            {
                System.out.println("Skip date:" + statTime.toLocalDate());
                continue; //skip
            }

            System.out.println("Not skipping :" + statTime.toLocalDate());


            final double v = values[i];

            //Runnable r = () ->
            //{

                builder.statName(statName)
                        .filterTypeName(resourceTypeName)
                        .machine(machine)
                        .statTimestamp(statTime
                        )
                        .statValue(v);

                statEntities.add(builder.build());
            //};
            //queue.add(r);
        }


        dao.saveAll(statEntities);

        //boss.startWorking(queue, false);

    }//-------------------------------------------

    boolean mustSkip(ResourceType resourceType)
    {
        if (resourceType == null)
            return true;

        String type = resourceType.getName();
        if (!this.statTypeName.equalsIgnoreCase(type))
        {
            System.out.println("Skipping type:" + type);
            return true;
        }

        System.out.println("Not skipping:" + type);
        return false;
    }
}
