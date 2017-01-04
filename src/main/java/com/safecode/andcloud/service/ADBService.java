package com.safecode.andcloud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * ADB 简单操作，对于模拟器来说
 *
 * @author sumy
 */
@Service
public class ADBService {

    private final static Logger logger = LoggerFactory.getLogger(ADBService.class);

    public void connect(String identifier) {
        String command[] = {"adb", "connect", identifier};
        try {
            Process process = Runtime.getRuntime().exec(command);
            InputStreamReader instream = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(instream);
            process.waitFor();
            String line = input.readLine();
            logger.debug("Adb Connect to " + identifier + " result - " + line);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void installAPk(String identifier, String apkpath) {
        String command[] = {"adb", "-s", identifier, "install", "-r", apkpath};
        try {
            Process process = Runtime.getRuntime().exec(command);
            InputStreamReader instream = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(instream);
            process.waitFor();
            String line = input.readLine();
            logger.debug("Adb install apk " + apkpath + " to" + identifier + " result - " + line);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startScreenCastService(String identifier, String ip, String port) {
        String command[] = {"adb", "-s", identifier, "shell", "am", "startservice", "-a", ip + ":" + port, "opensecurity.screencast/.StartScreenCast"};
        try {
            Process process = Runtime.getRuntime().exec(command);
            InputStreamReader instream = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(instream);
            process.waitFor();
            String line = input.readLine();
            logger.debug("Adb start ScreenCast for" + identifier + " result - " + line);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean aaptDumpApkInfo(String aaptversion, String apkpath, String logpath) {
        File aapt = new File(System.getProperty("user.dir")).toPath().resolve("aapt")
                .resolve(aaptversion).resolve("aapt").toFile(); // 获取aapt路径
        logger.debug("Use " + aapt.toString() + " to dump " + apkpath + " info, saved in " + logpath);
        String command[] = {aapt.toString(), "dump", "badging", apkpath};
        try {
            File logfile = new File(logpath);
            PrintWriter writer = new PrintWriter(logfile);
            Process process = Runtime.getRuntime().exec(command);
            InputStreamReader instream = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(instream);
            process.waitFor();
            String line = null;
            while ((line = input.readLine()) != null) {
                writer.println(line);
            }
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
