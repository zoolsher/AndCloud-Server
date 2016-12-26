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

    public void connectSIMByIP(String ipAddress) {
        String command[] = {"adb", "connect", ipAddress + ":5555"};
        try {
            Process process = Runtime.getRuntime().exec(command);
            InputStreamReader instream = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(instream);
            process.waitFor();
            String line = input.readLine();
            logger.debug("Adb Connect to " + ipAddress + ":5555 result - " + line);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
