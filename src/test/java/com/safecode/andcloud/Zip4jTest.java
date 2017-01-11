package com.safecode.andcloud;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

/**
 * 测试zip4j库的使用和基本输出信息
 *
 * @author sumy
 */
public class Zip4jTest {

    public static void main(String[] args) {
        try {
            ZipFile zipFile = new ZipFile("/home/sumy/QQliulanqi_561380.apk");
            List<FileHeader> headerList = zipFile.getFileHeaders();
            for (FileHeader fileHeader : headerList) {
                System.out.println(fileHeader.getFileName());
            }
            FileHeader afile = zipFile.getFileHeader("assets/skin/cache/skinNativeAPIRealv3.js");
            if (afile != null) {
                InputStream inputStream = zipFile.getInputStream(afile);
                Scanner scanner = new Scanner(inputStream);
                while (scanner.hasNext()) {
                    System.out.println(scanner.nextLine());
                }
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

}
