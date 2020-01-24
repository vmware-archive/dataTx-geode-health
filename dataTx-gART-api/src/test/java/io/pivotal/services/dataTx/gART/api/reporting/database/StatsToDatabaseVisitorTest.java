package io.pivotal.services.dataTx.gART.api.reporting.database;

import io.pivotal.services.dataTx.gART.api.dao.StatDao;
import io.pivotal.services.dataTx.geode.operations.stats.*;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Properties;

import static org.mockito.Mockito.*;

/**
 * Test for StatsToDatabaseVisitor
 * @author Gregory Green
 */
public class StatsToDatabaseVisitorTest
{

    @Test
    public void verify_visitor_save_data()
    {
        Properties props = new Properties();

        StatDao dao = mock(StatDao.class);

        StatsToDatabaseVisitor vistor = new StatsToDatabaseVisitor(dao);

        ResourceInst resourceInst = mock(ResourceInst.class);

        //StatValue
        StatValue statValue = mock(StatValue.class);
        StatValue[] statValues = {statValue};
        when(resourceInst.getStatValues()).thenReturn(statValues);

        //Time/Values
        long[] expectedTimes = {System.currentTimeMillis(),System.currentTimeMillis()};
        double[] expectedValues = {1.1, 1.2};

        when(statValue.getRawAbsoluteTimeStamps()).thenReturn(expectedTimes);
        when(statValue.getSnapshots()).thenReturn(expectedValues);

        //Stat description
        String statDescriptorName = "expectedStatDescriptorName";
        StatDescriptor statDescriptor = mock(StatDescriptor.class);
        when(statDescriptor.getName()).thenReturn(statDescriptorName);
        when(statValue.getDescriptor()).thenReturn(statDescriptor);

        GfStatsReader archive = mock(GfStatsReader.class);
        when(resourceInst.getArchive()).thenReturn(archive);

        String resourceTypeName = "expected=rtn";

        ResourceType resourceType = mock(ResourceType.class);

        when(resourceInst.getType()).thenReturn(resourceType);
        when(resourceType.getName()).thenReturn(resourceTypeName);

        ArchiveInfo archiveInfo = mock(ArchiveInfo.class);

        when(archive.getArchiveInfo()).thenReturn(archiveInfo);

        vistor.visitResourceInst(resourceInst);

        verify(dao,times(expectedTimes.length)).save(any());
    }
}