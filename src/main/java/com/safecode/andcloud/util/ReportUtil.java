package com.safecode.andcloud.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.safecode.andcloud.vo.CheckedAPIvo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by sharp on 2017/1/12.
 */
public class ReportUtil
{
    public static List<CheckedAPIvo> analysisAPIfromLog(String logPath, String pkgName)
    {
        List<CheckedAPIvo> checkedAPIs = new ArrayList<>();
        String splitSign = "Droidmon-apimonitor-" + pkgName + ":";
        try
        {
            Reader logcatFileReader = new FileReader(logPath);
            BufferedReader bufferedReader = new BufferedReader(logcatFileReader);

            Gson gson = new Gson();
            Properties properties = new Properties();
            try
            {
                properties.load(new FileInputStream("src/main/resources/API.properties"));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            String thisLine;
            try
            {
                while ((thisLine = bufferedReader.readLine()) != null)
                {
                    if(thisLine.indexOf(splitSign) == -1)
                    {
                        continue;
                    }

                    CheckedAPIvo api = new CheckedAPIvo();
                    String[] xLogValue = thisLine.split(splitSign, 2);
                    if (xLogValue.length == 2)
                    {
                        Map<String, Object> xLogMap = gson.fromJson(xLogValue[1], new TypeToken<Map<String, Object>>()
                        {
                        }.getType());

                        api.setCls(xLogMap.get("class").toString());
                        api.setMtd(xLogMap.get("method").toString());

                        if (xLogMap.get("return") != null)
                        {
                            api.setRet(xLogMap.get("return").toString());
                        }
                        if (xLogMap.get("args") != null)
                        {
                            api.setArgs(xLogMap.get("args").toString());
                        }
                        if (xLogMap.get("timestamp") != null)
                        {
                            api.setTimeStamp(xLogMap.get("timestamp").toString());
                        }

                        String thisType = properties.getProperty(api.getCls());
                        if(thisType != null)
                        {
                            api.setType(thisType);
                        }

                        checkedAPIs.add(api);
                    } else
                    {
                        continue;
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return checkedAPIs;

    }
}
