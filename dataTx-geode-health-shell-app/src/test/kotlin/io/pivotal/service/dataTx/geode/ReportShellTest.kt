package io.pivotal.service.dataTx.geode

import io.pivotal.service.dataTx.geode.health.ReportShell
import io.pivotal.services.dataTx.geode.health.api.dao.entity.StatDbType
import nyla.solutions.core.io.IO
import org.junit.Test
import org.junit.Assert.*
import java.nio.file.Paths
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReportShellTest
{


    @Test
    fun test_report_with_html_template()
    {
        var shell = ReportShell();

        var dayFilter = "5/12/2019";

        var statsFileOrDirPath  = "src/test/resources/stats";

        var reportFilePath = "target/output/out.html";


        var template = "src/test/resources/junit-template/user_report.txt";

        shell.htmlReport(statsFileOrDirPath, reportFilePath,dayFilter,template);

        assertTrue(Paths.get(reportFilePath).toFile().exists());


        var html = IO.readFile(reportFilePath)

        assertTrue(html, html.contains("Example Report"))
    }//-----------------------------

    @Test
    fun test_stats_saved_database()
    {
        var shell : ReportShell = ReportShell();
        var statsFileOrDirPath = "src/test/resources/stats/datanode_MacBook-Pro-5-18-01.gfs";
        var jdbcUrl ="jdbc:h2:~/test";
        var jdbcUsername ="sa";
        var jdbcPasword ="";
        var jdbcDbType = StatDbType.H2;


        shell.dbSync(statsFileOrDirPath,
                jdbcDbType,
                jdbcUrl,
                jdbcUsername,
                jdbcPasword,
                LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd"))
                ,""
                ,"",
                batchSize = 3);


    }//-------------------------------------------
    @Test
    fun test_report_with_default_html_template()
    {
        var shell = ReportShell();
        var dayFilter = "5/12/2019";

        var statsFileOrDirPath  = "src/test/resources/stats";

        //IO.mkdir("target/output")

        var reportFilePath = "target/output/defaults/myreport.html";


        var template = "";

        shell.htmlReport(statsFileOrDirPath, reportFilePath,dayFilter,template);

        assertTrue(Paths.get(reportFilePath).toFile().exists());


        var html = IO.readFile(reportFilePath)

        assertTrue(html, html.contains("<html>"))
        assertTrue(html, html.contains("<img"))
        assertTrue(html, html.contains("abandonedReadRequest"))
        assertTrue(html, html.contains("failedConnectionAttempts"))
        assertTrue(html, html.contains("connectionsTimedOut"))
    }//-----------------------------
}