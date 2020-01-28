# Apache Geode Health Shell App

This spring shell based application provides
utility to assist with the assessment of Apache Geode
based cluster deployments.


 Report included Key Data Point graphs
 Exposes shell commands to generate specific charts/reports based on statistic files

## Start Up
Starting the app shell

        java -jar target/dataTx-gART-shell-app-<version>.jar


## User guide

Example usages

        shell:>chart-cpu-above-threshold --input-file-path-dir ./stats --out-file-image-path /Users/ggreen/cpu.png --day-date 7/4/2019

        shell:>chart-jvm-avg-memory-above-threshold --input-file-path-dir ./stats --memory-percentage 50 --out-file-image-path ./jvmAvg.png



        shell:>help
        …

### AVAILABLE COMMANDS

**Built-In Commands**

Command | Notes
-------- | ----------------
 clear      |  Clear the shell screen. 
 exit, quit |  Exit the shell.
 help       | Display help about available commands.
 script     |  Read and execute commands from a file.
 stacktrace | Display the full stacktrace of the last error.

**Geode Health Commands**

Command             | Notes
------------------- | ----------------
chart-cpu-usage     | Builds a chart of the CPU Usage
chart-jvm-avg-memory-above-threshold | Builds a chart of the AVG JVM Memory Over a Threshold
chart-jvm-max-memory-above-threshold | Builds a chart of the MAX JVM Memory Over a Threshold
chart-par-new-collection-time-threshold | Builds a chart ParNew Collections times over a duration
chart-par-new-collections               | Builds a chart ParNew Garbage Collections
csv                         | Converts statistics to CSV
db-sync                     | Saves stats to database
html-report                 |Builds a complete report based on statistic files


# Command Operations Guide

## db-sync


The db-sync support storing statistics details to a 
relational database.

Support Databases

- [PostgresSQL](https://www.postgresql.org/)
- [MySQL](https://www.mysql.com/)
- [H2](https://www.h2database.com/html/main.html)

Example Usage

    db-sync --batch-size 1000 --jdbc-db-type MySQL --day-yyyymmddfilter "2019-08-12" --jdbc-username "user" --jdbc-pasword "password" --stats-file-or-dir-path "/users/ggreen/stats" --stat-type-name "CachePerfStats" --stat-name "creates" --jdbc-url "jdbc:mysql://<host>:<port>/mysql"
 
 
 Parameter          |   Notes
 ------------------ | ---------------------
jdbc-db-type        |  The target database to insert stats. Possible values (H2,MySql, PostgresDB)Ex: MySQL 
jdbc-url                | The database JDBC URL Ex: "jdbc:mysql://<host>:<port>/mysql" 
jdbc-username       |  The database username 
jdbc-pasword        |  The database password. This is optional, do nto provide if the database user does not have a password. 
batch-size          |  Batch insert size. Increase this can improve time it take to insert statistics into the database Ex: 1000
stats-file-or-dir-path |  The Apache Geode statistic file. Statistic file are normally have a gfs. You can also provide a directory and this command will recursive look for *.fs files. Ex: "/users/ggreen/stats"
day-yyyymmddfilter  |  The desired statistic day with format yyyy-MM-dd. Only statistics capture on this day will be saved Ex: "2019-08-12" 
stat-type-name          | The name of Apache Geode Statistic to capture. See [Geode Statistics List](https://geode.apache.org/docs/guide/12/reference/statistics/statistics_list.html) for possible values.  Ex: "CachePerfStats" 
stat-name               | The static for the *stat-type-name*.  Ex: "creates" 

