package com.safecode.andcloud.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从aapt dump log中查找相关信息
 *
 * @author sumy
 */
public class AAPTDumpLogInfoFinderUtil {
    public static final String LABEL_DEFAULT = "default";
    public static final String LABEL_APPLICATION = "application";
    public static final String ICON_APPLICATION = "application";

    private static final String FIELD_PATTERN = "(.[^']*)";

    private String versionCode;
    private String versionName;
    private String launchActivity;
    private String packages;
    private String sdkversion;
    private String targetsdkversion;
    private Set<String> userpermissions;
    private Map<String, String> labels;
    private Map<String, String> icons;

    public AAPTDumpLogInfoFinderUtil(File dumplog) {
        userpermissions = new HashSet<String>();
        labels = new HashMap<String, String>();
        icons = new HashMap<String, String>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(dumplog)));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("package")) {
                    extractPackage(line);
                    extractVersionName(line);
                    extractVersionCode(line);
                }
                if (line.startsWith("launchable-activity")) {
                    extractLaunchActivity(line);
                }
                if (line.startsWith("uses-permission")) {
                    extractUserPermission(line);
                }
                if (line.startsWith("application")) {
                    extractLabel(line);
                    extractIcon(line);
                }
                if (line.startsWith("sdkVersion")) {
                    extractSdkVersion(line);
                }
                if (line.startsWith("targetSdkVersion")) {
                    extractTargetSdkVersion(line);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getLaunchActivity() {
        return launchActivity;
    }

    public String getPackages() {
        return packages;
    }

    public String getSdkversion() {
        return sdkversion;
    }

    public String getTargetsdkversion() {
        return targetsdkversion;
    }

    public Set<String> getUserpermissions() {
        return userpermissions;
    }

    public String getIcon(String icon) {
        return icons.get(icon);
    }

    public String getLabel(String label) {
        return labels.get(label);
    }

    public Set<String> getIconNameSet() {
        return icons.keySet();
    }

    public Set<String> getLabelNameSet() {
        return labels.keySet();
    }

    public void dumpInformation() {
        System.out.println("package: " + packages);
        System.out.println("sdkVersion: " + sdkversion);
        System.out.println("targetSdkVersion: " + targetsdkversion);
        System.out.println("versionCode: " + versionCode);
        System.out.println("versionName: " + versionName);
        System.out.println("launchactivity: " + launchActivity);
        System.out.println("userpermissions: ");
        for (String permission : userpermissions) {
            System.out.println("\t" + permission);
        }
        System.out.println("application-label: ");
        for (String label : labels.keySet()) {
            System.out.println("\t" + label + "-" + labels.get(label));
        }
        System.out.println("application-icon: ");
        for (String icon : icons.keySet()) {
            System.out.println("\t" + icon + "-" + icons.get(icon));
        }
    }

    private void extractVersionCode(String output) {
        Pattern patn = Pattern.compile("versionCode='" + FIELD_PATTERN + "'");
        Matcher match = patn.matcher(output);
        if (match.find()) {
            versionCode = match.group(1).trim();
        }
    }

    private void extractVersionName(String output) {
        Pattern patn = Pattern.compile("versionName='" + FIELD_PATTERN + "'");
        Matcher match = patn.matcher(output);
        if (match.find()) {
            versionName = match.group(1).trim();
        }
    }

    private void extractSdkVersion(String output) {
        Pattern patn = Pattern.compile("sdkVersion:'" + FIELD_PATTERN + "'");
        Matcher match = patn.matcher(output);
        if (match.find()) {
            sdkversion = match.group(1).trim();
        }
    }

    private void extractTargetSdkVersion(String output) {
        Pattern patn = Pattern.compile("targetSdkVersion:'" + FIELD_PATTERN + "'");
        Matcher match = patn.matcher(output);
        if (match.find()) {
            targetsdkversion = match.group(1).trim();
        }
    }

    private void extractLaunchActivity(String output) {
        Pattern patn = Pattern.compile("launchable-activity: name='" + FIELD_PATTERN + "'");
        Matcher match = patn.matcher(output);
        if (match.find()) {
            launchActivity = match.group(1).trim();
        }
    }

    private void extractPackage(String output) {
        Pattern patn = Pattern.compile("package: name='" + FIELD_PATTERN + "'");
        Matcher match = patn.matcher(output);
        if (match.find()) {
            packages = match.group(1).trim();
        }

    }

    private void extractUserPermission(String output) {
        Pattern patn = Pattern.compile("uses-permission: name='" + FIELD_PATTERN + "'");
        Matcher match = patn.matcher(output);
        if (match.find()) {
            userpermissions.add(match.group(1).trim());
        }
    }

    private void extractLabel(String output) {
        Pattern patn = Pattern.compile("application: label='" + FIELD_PATTERN + "'");
        Matcher match = patn.matcher(output);
        if (match.find()) {
            labels.put(LABEL_APPLICATION, match.group(1).trim());
        }
        patn = Pattern.compile("application-label:'" + FIELD_PATTERN + "'");
        match = patn.matcher(output);
        if (match.find()) {
            labels.put(LABEL_DEFAULT, match.group(1).trim());
        }
        patn = Pattern.compile("application-label-" + FIELD_PATTERN + ":'" + FIELD_PATTERN + "'");
        match = patn.matcher(output);
        if (match.find()) {
            labels.put(match.group(1).trim(), match.group(2).trim());
        }
    }

    private void extractIcon(String output) {
        Pattern patn = Pattern.compile("icon='" + FIELD_PATTERN + "'");
        Matcher match = patn.matcher(output);
        if (match.find()) {
            icons.put(ICON_APPLICATION, match.group(1).trim());
        }
        patn = Pattern.compile("application-icon-" + FIELD_PATTERN + ":'" + FIELD_PATTERN + "'");
        match = patn.matcher(output);
        if (match.find()) {
            icons.put(match.group(1).trim(), match.group(2).trim());
        }
    }

    public static void main(String[] args) {
        File file = new File("aaptdump.log");
        new AAPTDumpLogInfoFinderUtil(file).dumpInformation();
    }
}
