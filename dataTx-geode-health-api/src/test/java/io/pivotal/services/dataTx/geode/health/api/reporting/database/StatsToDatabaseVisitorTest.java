package io.pivotal.services.dataTx.geode.health.api.reporting.database;

import io.pivotal.services.dataTx.geode.health.api.dao.StatDao;
import io.pivotal.services.dataTx.geode.operations.stats.*;
import static org.junit.jupiter.api.Assertions.*;

import lombok.NonNull;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Properties;

import static org.mockito.Mockito.*;

/**
 * Test for StatsToDatabaseVisitor
 * @author Gregory Green
 */
public class StatsToDatabaseVisitorTest
{

    @Test
    void test_mustSkip()
    {
        StatDao dao = mock(StatDao.class);
        String expectedType = "expectType";
        String expectedStatName = "expected-stat";

        int threadCount = 2;

        StatsToDatabaseVisitor v = StatsToDatabaseVisitor
                .builder()
                .dao(dao)
                .dayFilter(LocalDate.now())
                .statTypeName(expectedType)
                .statName(expectedStatName)
                .batchSize(20)
                .build();


        ResourceType skipStat = mock(ResourceType.class);

        assertTrue(v.mustSkip(skipStat));

        ResourceType nonSkipStat = mock(ResourceType.class);
        when(nonSkipStat.getName()).thenReturn(expectedType);
        assertFalse(v.mustSkip(nonSkipStat));

    }



    @Test
    public void verify_visitor_save_data()
    {
        String resourceTypeName = "expected=rtn";
        String statName = "expected=statName";

        Properties props = new Properties();

        StatDao dao = mock(StatDao.class);

        LocalDate dayFilter = LocalDate.now();

        StatsToDatabaseVisitor vistor = StatsToDatabaseVisitor
                .builder()
                .dao(dao).dayFilter(dayFilter)
                .batchSize(23)
                .statTypeName(resourceTypeName)
                .statName(statName)
                .batchSize(1323)
                .build();

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



        ResourceType resourceType = mock(ResourceType.class);

        when(resourceInst.getType()).thenReturn(resourceType);
        when(resourceInst.getStatValue(anyString())).thenReturn(statValue);
        when(resourceType.getName()).thenReturn(resourceTypeName);

        ArchiveInfo archiveInfo = mock(ArchiveInfo.class);

        when(archive.getArchiveInfo()).thenReturn(archiveInfo);

        vistor.visitResourceInst(resourceInst);

        //verify(dao,times(expectedTimes.length)).save(any());
    }
}