package com.safecode.andcloud.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 线程 Worker - 用于从 LogCat 中抓取设备的 Log，然后将其输出到指定位置
 *
 * @author sumy
 */
public class LogcatWorker extends Thread {

    private final static Logger logger = LoggerFactory.getLogger(LogcatWorker.class);

    private boolean isStop = false;
    private String outputFile;
    private String ipAddress;

    public LogcatWorker(String outputFile, String ipAddress) {
        this.outputFile = outputFile;
        this.ipAddress = ipAddress;
        this.isStop = false;
    }

    public void stopme() {
        this.isStop = true;
    }

    @Override
    public void run() {
        String[] command = {"adb", "logcat", "", ""};
        if (ipAddress != null) {
            command[2] = "-s";
            command[3] = ipAddress + ":5555";
        }
        File file = new File(outputFile);
        PrintWriter logFileWriter = null;
        try {
            OutputStream outputStream = new FileOutputStream(file);
            logFileWriter = new PrintWriter(outputStream);
        } catch (FileNotFoundException e) {
            logger.error("Create LogCat File Error.", e);
        }
        try {
            Process process = Runtime.getRuntime().exec(command);
            InputStreamReader instream = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(instream);
            // process.waitFor();
            String line = null;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                if (logFileWriter != null) {
                    logFileWriter.println(line);
                }
                if (isStop) {
                    break;
                }
            }
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (logFileWriter != null) {
                logFileWriter.close();
            }
        }
    }
}
