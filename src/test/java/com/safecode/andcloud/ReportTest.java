package com.safecode.andcloud;

/**
 * Created by sharp on 2017/1/10.
 */
public class ReportTest
{
    public static void main(String[] args)
    {
        ReportService dars = new ReportService("F:\\29-4-0-10.txt","111","eu.chainfire.supersu");
        DynamicAnalysisReport report = dars.processLogcatToReport();

        dars.saveReport(report);
//        System.out.println(report.getAPI_CMD());
    }
}
