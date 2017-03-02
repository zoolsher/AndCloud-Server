package com.safecode.andcloud.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.safecode.andcloud.model.ReportItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sharp on 2017/1/10.
 */
public class ReportParser {

    private static final Logger logger = LoggerFactory.getLogger(ReportParser.class);


    private ReportParser() {
    }

    public static List<ReportItem> processLogcatToReport(String logcatPath, String pkgName) {
        logger.debug("Process logcat report for " + pkgName);

        String splitSign = String.format("Droidmon-apimonitor-%s:", pkgName);
        List<ReportItem> itemlist = new ArrayList<ReportItem>();

        try {
            Reader logcatFileReader = new FileReader(logcatPath);
            BufferedReader bufferedReader = new BufferedReader(logcatFileReader);
            String thisLine;

            Gson gson = new Gson();
            try {
                while ((thisLine = bufferedReader.readLine()) != null) {

                    if (!thisLine.contains(splitSign)) {
                        continue;
                    }

                    String[] xLogValue = thisLine.split(splitSign, 2);
                    if (xLogValue.length == 2) {
                        logger.debug("Progress..." + thisLine);
                        ReportItem reportItem = new ReportItem();
                        Map<String, Object> xLogMap;
                        try {
                            xLogMap = gson.fromJson(xLogValue[1], new TypeToken<Map<String, Object>>() {
                            }.getType());
                        } catch (Exception e) {
                            logger.error("Parser error. " + thisLine, e);
                            continue;
                        }
                        reportItem.setCls(xLogMap.get("class").toString());
                        reportItem.setMtd(xLogMap.get("method").toString());

                        reportItem.setRet(xLogMap.getOrDefault("return", "").toString());
                        reportItem.setArgs(xLogMap.getOrDefault("args", "").toString());
                        reportItem.setTimestamp(Long.parseLong(xLogMap.getOrDefault("timestamp", "-1").toString()));

//                        String methodDescript = "{\"class\":\"" + cls + "\",\"method\":\"" + mtd + "\",\"return\":\"" + ret + "\",\"arguments\":\"" + args + "\",\"timestamp\":\"" + timestamp + "\"}";

                        String cls = reportItem.getCls();
                        if (cls.contains("android.util.Base64")) {
                            reportItem.setType(ReportItem.TYPE_API_BASE64);
                        }
                        if ((cls.contains("libcore.io")) || (cls.contains("android.app.SharedPreferencesImpl$EditorImpl"))) {
                            reportItem.setType(ReportItem.TYPE_API_FILEIO);
                        }
                        if (cls.contains("java.lang.reflect")) {
                            reportItem.setType(ReportItem.TYPE_API_RELECT);
                        }
                        if ((cls.contains("android.content.ContentResolver"))
                                || (cls.contains("android.location.Location"))
                                || (cls.contains("android.media.AudioRecord"))
                                || (cls.contains("android.media.MediaRecorder"))
                                || (cls.contains("android.os.SystemProperties"))) {
                            reportItem.setType(ReportItem.TYPE_API_SYSPROP);
                        }
                        if ((cls.contains("javax.crypto.spec.SecretKeySpec"))
                                || (cls.contains("javax.crypto.Cipher"))
                                || (cls.contains("javax.crypto.Mac"))) {
                            reportItem.setType(ReportItem.TYPE_API_CRYPTO);
                        }
                        if ((cls.contains("android.accounts.AccountManager"))
                                || (cls.contains("android.app.ApplicationPackageManager"))
                                || (cls.contains("android.app.NotificationManager"))
                                || (cls.contains("android.net.ConnectivityManager"))
                                || (cls.contains("android.content.BroadcastReceiver"))) {
                            reportItem.setType(ReportItem.TYPE_API_ACNTMNGER);
                        }
                        if ((cls.contains("android.telephony.TelephonyManager"))
                                || (cls.contains("android.net.wifi.WifiInfo"))
                                || (cls.contains("android.os.Debug"))) {
                            reportItem.setType(ReportItem.TYPE_API_DEVICEINFO);
                        }
                        if ((cls.contains("dalvik.system.BaseDexClassLoader"))
                                || (cls.contains("dalvik.system.DexFile"))
                                || (cls.contains("dalvik.system.DexClassLoader"))
                                || (cls.contains("dalvik.system.PathClassLoader"))) {
                            reportItem.setType(ReportItem.TYPE_API_DEXLOADER);
                        }
                        if ((cls.contains("java.lang.Runtime"))
                                || (cls.contains("java.lang.ProcessBuilder"))
                                || (cls.contains("java.io.FileOutputStream"))
                                || (cls.contains("java.io.FileInputStream"))
                                || (cls.contains("android.os.Process"))) {
                            reportItem.setType((ReportItem.TYPE_API_CMD));
                        }
                        if (cls.contains("android.content.ContentValues")) {
                            reportItem.setType(ReportItem.TYPE_API_CNTVAL);
                        }
                        if (cls.contains("android.telephony.SmsManager")) {
                            reportItem.setType(ReportItem.TYPE_API_SMS);
                        }
                        if ((cls.contains("java.net.URL"))
                                || (cls.contains("org.apache.http.impl.client.AbstractHttpClient"))) {
                            reportItem.setType(ReportItem.TYPE_API_NET);
                        }
                        itemlist.add(reportItem);
                    }
                }
            } catch (IOException e) {
                logger.error("Logcat report file error.", e);
            }
        } catch (FileNotFoundException e) {
            logger.error("Logcat report file not found.", e);
        }

        return itemlist;
    }

}
