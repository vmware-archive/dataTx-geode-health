package io.pivotal.services.dataTx.gART.api.reporting.database;

import io.pivotal.services.dataTx.gART.api.dao.StatDao;
import io.pivotal.services.dataTx.gART.api.dao.entity.StatEntity;
import io.pivotal.services.dataTx.geode.operations.stats.ResourceInst;
import io.pivotal.services.dataTx.geode.operations.stats.ResourceType;
import io.pivotal.services.dataTx.geode.operations.stats.StatDescriptor;
import io.pivotal.services.dataTx.geode.operations.stats.StatValue;
import io.pivotal.services.dataTx.geode.operations.stats.visitors.StatsVisitor;

import java.time.Instant;
import java.time.ZoneId;

/**
 * Save status data to database
 *
 * @author Gregory Green
 */
public class StatsToDatabaseVisitor implements StatsVisitor
{
    private StatDao dao;

    public StatsToDatabaseVisitor(StatDao dao)
    {
        this.dao = dao;
    }//-------------------------------------------

    @Override
    public void visitResourceInst(ResourceInst resourceInst)
    {
        String name = resourceInst.getName();

        String machine = resourceInst.getArchive().getArchiveInfo().getMachine();

        ResourceType resourceType= resourceInst.getType();

        String resourceTypeName=  resourceType.getName();

        StatValue[] statValues = resourceInst.getStatValues();
        if(statValues == null)
            return;

        for (StatValue statValue : statValues)
        {
            StatEntity.StatEntityBuilder builder = StatEntity.builder();

            String statName = statValue.getDescriptor().getName();

            StatDescriptor statDescriptor = resourceInst.getType().getStat(statName);

            long[] times = statValue.getRawAbsoluteTimeStamps();
            double[] values = statValue.getSnapshots();

            for (int i = 0; i < values.length; i++)
            {

                builder.statName(statName)
                        .filterTypeName(resourceTypeName)
                        .machine(machine)
                        .statTimestamp(Instant.ofEpochMilli(times[i])
                            .atZone(ZoneId.systemDefault()).toLocalDateTime()
                        )
                        .statValue(values[i]);

                dao.save(builder.build());

            }
        }

    }//------------------------------------------------
}
