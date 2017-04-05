package com.safecode.andcloud.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.safecode.andcloud.model.ReportItem;
import com.safecode.andcloud.vo.APIReportItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

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

        Set<APIReportItem> API_BASE64 = new HashSet<APIReportItem>();
        Set<APIReportItem> API_FILEIO = new HashSet<APIReportItem>();
        Set<APIReportItem> API_RELECT = new HashSet<APIReportItem>();
        Set<APIReportItem> API_SYSPROP = new HashSet<APIReportItem>();
        Set<APIReportItem> API_CNTRSLVR = new HashSet<APIReportItem>();
        Set<APIReportItem> API_CNTVAL = new HashSet<APIReportItem>();
        Set<APIReportItem> API_BINDER = new HashSet<APIReportItem>();
        Set<APIReportItem> API_CRYPTO = new HashSet<APIReportItem>();
        Set<APIReportItem> API_ACNTMNGER = new HashSet<APIReportItem>();
        Set<APIReportItem> API_DEVICEINFO = new HashSet<APIReportItem>();
        Set<APIReportItem> API_NET = new HashSet<APIReportItem>();
        Set<APIReportItem> API_DEXLOADER = new HashSet<APIReportItem>();
        Set<APIReportItem> API_CMD = new HashSet<APIReportItem>();
        Set<APIReportItem> API_SMS = new HashSet<APIReportItem>();

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
                        APIReportItem reportItem = new APIReportItem();
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
                        if (xLogMap.containsKey("return")) {
                            reportItem.setRet(xLogMap.get("return").toString());
                        } else {
                            reportItem.setRet("No Return Data");
                        }

                        if (xLogMap.containsKey("args")) {
                            reportItem.setArgs(xLogMap.get("args").toString());
                        } else {
                            reportItem.setArgs("No Arguments Passed");
                        }

                        reportItem.setTimestamp((long) Double.parseDouble(xLogMap.getOrDefault("timestamp", "-1").toString()));

                        String cls = reportItem.getCls();
                        if (cls.contains("android.util.Base64")) {
                            if (reportItem.getMtd().contains("decode")) {
                                List<String> argslist = gson.fromJson(reportItem.getArgs(), new TypeToken<List<String>>() {
                                }.getType());
                                if (StringUtil.isBase64(argslist.get(0))) {
                                    reportItem.setExt(new String(Base64.getDecoder().decode(argslist.get(0))));
                                }
                            }
                            API_BASE64.add(reportItem);
                        }
                        if ((cls.contains("libcore.io")) || (cls.contains("android.app.SharedPreferencesImpl$EditorImpl"))) {
                            API_FILEIO.add(reportItem);
                        }
                        if (cls.contains("java.lang.reflect")) {
                            API_RELECT.add(reportItem);
                        }
                        if ((cls.contains("android.content.ContentResolver"))
                                || (cls.contains("android.location.Location"))
                                || (cls.contains("android.media.AudioRecord"))
                                || (cls.contains("android.media.MediaRecorder"))
                                || (cls.contains("android.os.SystemProperties"))) {
                            API_SYSPROP.add(reportItem);
                        }
                        if (cls.contains("android.app.Activity")
                                || cls.contains("android.app.ContextImpl")
                                || cls.contains("android.app.ActivityThread")) {
                            API_BINDER.add(reportItem);
                        }
                        if ((cls.contains("javax.crypto.spec.SecretKeySpec"))
                                || (cls.contains("javax.crypto.Cipher"))
                                || (cls.contains("javax.crypto.Mac"))) {
                            API_CRYPTO.add(reportItem);
                        }
                        if ((cls.contains("android.accounts.AccountManager"))
                                || (cls.contains("android.app.ApplicationPackageManager"))
                                || (cls.contains("android.app.NotificationManager"))
                                || (cls.contains("android.net.ConnectivityManager"))
                                || (cls.contains("android.content.BroadcastReceiver"))) {
                            API_ACNTMNGER.add(reportItem);
                        }
                        if ((cls.contains("android.telephony.TelephonyManager"))
                                || (cls.contains("android.net.wifi.WifiInfo"))
                                || (cls.contains("android.os.Debug"))) {
                            API_DEVICEINFO.add(reportItem);
                        }
                        if ((cls.contains("dalvik.system.BaseDexClassLoader"))
                                || (cls.contains("dalvik.system.DexFile"))
                                || (cls.contains("dalvik.system.DexClassLoader"))
                                || (cls.contains("dalvik.system.PathClassLoader"))) {
                            API_DEXLOADER.add(reportItem);
                        }
                        if ((cls.contains("java.lang.Runtime"))
                                || (cls.contains("java.lang.ProcessBuilder"))
                                || (cls.contains("java.io.FileOutputStream"))
                                || (cls.contains("java.io.FileInputStream"))
                                || (cls.contains("android.os.Process"))) {
                            API_CMD.add(reportItem);
                        }
                        if (cls.contains("android.content.ContentValues")) {
                            API_CNTVAL.add(reportItem);
                        }
                        if (cls.contains("android.telephony.SmsManager")) {
                            API_SMS.add(reportItem);
                        }
                        if ((cls.contains("java.net.URL"))
                                || (cls.contains("org.apache.http.impl.client.AbstractHttpClient"))) {
                            API_NET.add(reportItem);
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("Logcat report file error.", e);
            }
            for (APIReportItem reportItem : API_BASE64) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_BASE64));
            }
            for (APIReportItem reportItem : API_FILEIO) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_FILEIO));
            }
            for (APIReportItem reportItem : API_RELECT) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_RELECT));
            }
            for (APIReportItem reportItem : API_SYSPROP) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_SYSPROP));
            }
            for (APIReportItem reportItem : API_CNTRSLVR) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_CNTRSLVR));
            }
            for (APIReportItem reportItem : API_CNTVAL) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_CNTVAL));
            }
            for (APIReportItem reportItem : API_BINDER) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_BINDER));
            }
            for (APIReportItem reportItem : API_CRYPTO) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_CRYPTO));
            }
            for (APIReportItem reportItem : API_ACNTMNGER) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_ACNTMNGER));
            }
            for (APIReportItem reportItem : API_DEVICEINFO) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_DEVICEINFO));
            }
            for (APIReportItem reportItem : API_NET) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_NET));
            }
            for (APIReportItem reportItem : API_DEXLOADER) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_DEXLOADER));
            }
            for (APIReportItem reportItem : API_CMD) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_CMD));
            }
            for (APIReportItem reportItem : API_SMS) {
                itemlist.add(reportItem.toReportItem(ReportItem.TYPE_API_SMS));
            }
        } catch (FileNotFoundException e) {
            logger.error("Logcat report file not found.", e);
        }

        return itemlist;
    }

}
