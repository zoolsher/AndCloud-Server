package com.safecode.andcloud.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.safecode.andcloud.dao.DynamicAnalysisReportDao;
import com.safecode.andcloud.model.DynamicAnalysisReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

/**
 * Created by sharp on 2017/1/10.
 */
@Service
public class DynamicAnalysisReportService
{
    private String logcatPath;
    private String emulatorId;
    private String pkgName;

    @Autowired
    private DynamicAnalysisReportDao dynamicAnalysisReportDao;

    public DynamicAnalysisReportService(String logcatPath, String emulatorId, String pkgName)
    {
        this.logcatPath = logcatPath;
        this.emulatorId = emulatorId;
        this.pkgName = pkgName;
    }

    public void saveReport(DynamicAnalysisReport report)
    {
        dynamicAnalysisReportDao.saveOrUpdate(report);
    }

    public DynamicAnalysisReport processLogcatToReport()
    {
        DynamicAnalysisReport report = new DynamicAnalysisReport();
        report.setEmulator_id(emulatorId);
        report.setPkgName(pkgName);

        String splitSign = "Droidmon-apimonitor-" + pkgName + ":";

        try
        {
            Reader logcatFileReader = new FileReader(logcatPath);
            BufferedReader bufferedReader = new BufferedReader(logcatFileReader);
            String thisLine;

            Gson gson = new Gson();
            int counter = 0;
            try
            {
                while ((thisLine = bufferedReader.readLine()) != null)
                {
                    counter++;
                    System.out.println(counter);

                    if(thisLine.indexOf(splitSign) == -1)
                    {
                        continue;
                    }

                    String[] xLogValue = thisLine.split(splitSign, 2);
                    if (xLogValue.length == 2)
                    {

                        System.out.println("----------------------------------------");
                        Map<String, Object> xLogMap = gson.fromJson(xLogValue[1], new TypeToken<Map<String, Object>>()
                        {
                        }.getType());

                        String cls = xLogMap.get("class").toString();
                        String mtd = xLogMap.get("method").toString();
                        String ret;
                        String args;
                        String timestamp;

                        if (xLogMap.get("return") != null)
                        {
                            ret = xLogMap.get("return").toString();
                        } else
                        {
                            ret = "No Return Data";
                        }
                        if (xLogMap.get("args") != null)
                        {
                            args = xLogMap.get("args").toString();
                        } else
                        {
                            args = "No Arguments Passed";
                        }
                        if (xLogMap.get("timestamp") != null)
                        {
                            timestamp = xLogMap.get("timestamp").toString();
                        } else
                        {
                            timestamp = "null";
                        }
                        String methodDescript = "{\"class\":\"" + cls + "\",\"method\":\"" + mtd + "\",\"return\":\"" + ret + "\",\"arguments\":\"" + args + "\",\"timestamp\":\"" + timestamp + "\"}";

                        if (cls.indexOf("android.util.Base64") != -1)
                        {
                            report.getAPI_BASE64().add(methodDescript);
                        }
                        if ((cls.indexOf("libcore.io") != -1) || (cls.indexOf("android.app.SharedPreferencesImpl$EditorImpl") != -1))
                        {
                            report.getAPI_FILEIO().add(methodDescript);
                        }
                        if (cls.indexOf("java.lang.reflect") != -1)
                        {
                            report.getAPI_RELECT().add(methodDescript);
                        }
                        if ((cls.indexOf("android.content.ContentResolver") != -1) || (cls.indexOf("android.location.Location") != -1) || (cls.indexOf("android.media.AudioRecord") != -1) || (cls.indexOf("android.media.MediaRecorder") != -1) || (cls.indexOf("android.os.SystemProperties") != -1))
                        {
                            report.getAPI_SYSPROP().add(methodDescript);
                        }
                        if ((cls.indexOf("javax.crypto.spec.SecretKeySpec") != -1) || (cls.indexOf("javax.crypto.Cipher") != -1) || (cls.indexOf("javax.crypto.Mac") != -1))
                        {
                            report.getAPI_CRYPTO().add(methodDescript);
                        }
                        if ((cls.indexOf("android.accounts.AccountManager") != -1) || (cls.indexOf("android.app.ApplicationPackageManager") != -1) || (cls.indexOf("android.app.NotificationManager") != -1) || (cls.indexOf("android.net.ConnectivityManager") != -1) || (cls.indexOf("android.content.BroadcastReceiver") != -1))
                        {
                            report.getAPI_ACNTMNGER().add(methodDescript);
                        }
                        if ((cls.indexOf("android.telephony.TelephonyManager") != -1) || (cls.indexOf("android.net.wifi.WifiInfo") != -1) || (cls.indexOf("android.os.Debug") != -1))
                        {
                            report.getAPI_DEVICEINFO().add(methodDescript);
                        }
                        if ((cls.indexOf("dalvik.system.BaseDexClassLoader") != -1) || (cls.indexOf("dalvik.system.DexFile") != -1) || (cls.indexOf("dalvik.system.DexClassLoader") != -1) || (cls.indexOf("dalvik.system.PathClassLoader") != -1))
                        {
                            report.getAPI_DEXLOADER().add(methodDescript);
                        }
                        if ((cls.indexOf("java.lang.Runtime") != -1) || (cls.indexOf("java.lang.ProcessBuilder") != -1) || (cls.indexOf("java.io.FileOutputStream") != -1) || (cls.indexOf("java.io.FileInputStream") != -1) || (cls.indexOf("android.os.Process") != -1))
                        {
                            report.getAPI_CMD().add(methodDescript);
                        }
                        if (cls.indexOf("android.content.ContentValues") != -1)
                        {
                            report.getAPI_CNTVAL().add(methodDescript);
                        }
                        if (cls.indexOf("android.telephony.SmsManager") != -1)
                        {
                            report.getAPI_SMS().add(methodDescript);
                        }
                        if ((cls.indexOf("java.net.URL") != -1) || (cls.indexOf("org.apache.http.impl.client.AbstractHttpClient") != -1))
                        {
                            report.getAPI_NET().add(methodDescript);
                        }

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

        return report;
    }

}
