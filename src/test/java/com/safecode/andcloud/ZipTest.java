package com.safecode.andcloud;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * 测试类库的ZIP解压缩能力
 *
 * @author sumy
 */
public class ZipTest {

    public static void main(String[] args) {

        try {
            ZipFile zipFile = new ZipFile("/home/sumy/QQliulanqi_561380.apk");
            Enumeration<ZipArchiveEntry> zipentries = zipFile.getEntries();
            while (zipentries.hasMoreElements()) {
                ZipArchiveEntry zipArchiveEntry = zipentries.nextElement();
                System.out.println(zipArchiveEntry.getName());
            }

            ZipArchiveEntry aentry = zipFile.getEntry("assets/comparePrice/html/toolbar.html");
            InputStream inputStream = zipFile.getInputStream(aentry);
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNext()) {
                System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
