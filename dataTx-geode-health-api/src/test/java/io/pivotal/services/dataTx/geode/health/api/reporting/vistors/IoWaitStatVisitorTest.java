package io.pivotal.services.dataTx.geode.health.api.reporting.vistors;

import io.pivotal.services.dataTx.geode.office.StatsToChart;
import nyla.solutions.core.data.clock.Day;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Config;
import nyla.solutions.office.chart.Chart;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

public class IoWaitStatVisitorTest
{
    private File ioWaitFileStats = Paths.get(Config.getProperty("ioWaitFileStats")).toFile();

    @Test
    public void test_ioWait() throws Exception {
        IoWaitStatVisitor v = new IoWaitStatVisitor(new Day("7/30/2019"));


        StatsToChart chartMaker = new StatsToChart(v);

        Chart chart = chartMaker.convert(ioWaitFileStats);

        IO.delete(new File("target/ioWait.png"));

        IO.writeFile("target/out.png",chart.getBytes());

    }
}