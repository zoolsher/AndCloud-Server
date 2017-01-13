package com.safecode.andcloud;

import com.safecode.andcloud.util.ReportUtil;
import com.safecode.andcloud.vo.CheckedAPIvo;

import java.util.List;

/**
 * Created by sharp on 2017/1/10.
 */
public class ReportTest
{
    public static void main(String[] args)
    {
        List<CheckedAPIvo> result = ReportUtil.analysisAPIfromLog("F:\\29-4-0-10.txt","eu.chainfire.supersu");

        for(CheckedAPIvo apIvo: result)
        {
            System.out.println(apIvo.getCls());
            System.out.println(apIvo.getMtd());
            System.out.println(apIvo.getType());
            System.out.println(apIvo.getTimeStamp());
        }
    }
}
