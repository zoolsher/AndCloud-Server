package com.safecode.andcloud;

import com.safecode.andcloud.model.DynamicAnalysisReport;
import com.safecode.andcloud.service.DynamicAnalysisReportService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sharp on 2017/1/10.
 */
public class ReportTest
{
    public static void main(String[] args)
    {
        DynamicAnalysisReportService dars = new DynamicAnalysisReportService("F:\\29-4-0-10.txt","111","eu.chainfire.supersu");
        DynamicAnalysisReport report = dars.processLogcatToReport();

        dars.saveReport(report);
//        System.out.println(report.getAPI_CMD());
    }
}
