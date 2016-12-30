package com.safecode.andcloud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

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

}
