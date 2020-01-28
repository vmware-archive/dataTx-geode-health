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

        Report Shell
                chart-cpu-above-threshold: Builds a chart of the CPU Usage Over a Threshold
                chart-jvm-avg-memory-above-threshold: Builds a chart of the AVG JVM Memory Over a Threshold
                chart-jvm-max-memory-above-threshold: Builds a chart of the MAX JVM Memory Over a Threshold
                chart-par-new-collection-time-threshold: Builds a chart ParNew Collections times over a duration
                chart-par-new-collections: Builds a chart ParNew Garbage Collect
                html-report: Builds a complete report based on statistic files

