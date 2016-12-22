package com.safecode.andcloud;

import com.safecode.andcloud.worker.LogcatWorker;

/**
 * 测试封装 Shell 的方式获取 adb logcat 的输出
 *
 * @author sumy
 */
public class ADBLogCatTest {

    public static void main(String[] args) {
        LogcatWorker thread = new LogcatWorker("log.txt", null);
        thread.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.stopme();
    }
}
