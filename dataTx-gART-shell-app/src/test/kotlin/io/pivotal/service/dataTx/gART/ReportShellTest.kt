package io.pivotal.service.dataTx.gART

import io.pivotal.services.dataTx.gART.api.dao.entity.StatDbType
import nyla.solutions.core.io.IO
import org.junit.Test
import org.junit.Assert.*
import java.nio.file.Paths

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
        var statsFileOrDirPath = "";
        var jdbcUrl ="";
        var jdbcUsername ="";
        var jdbcPasword ="";
        var jdbcDbType = StatDbType.PostgresDB;

        shell.dbSync(statsFileOrDirPath,
                jdbcDbType,
                jdbcUrl,
                jdbcUsername,
                jdbcPasword)
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