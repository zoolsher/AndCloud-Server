package com.safecode.andcloud.util;

import org.dom4j.*;

import java.io.*;
import java.util.*;

/**
 * 工具类，获取AndroidManifest中的信息
 */
public class AndroidManifest {
    public static String NS_ANDROID_URI = "http://schemas.android.com/apk/res/android";

    private String packageName;

    private String versionCode;
    private String versionName;

    private List<String> permissions;

    private String mainActivity;
    private List<String> activities;
    private List<String> exportedactivities;
    private List<String> services;
    private List<String> exportedservices;
    private List<String> receivers;
    //    private List<String> exportedreceivers;
    private List<String> providers;
//    private List<String> exportedproviders;

    private boolean allowBakcup = false;
    private boolean debuggable = false;

    private AXMLPrinter printer;

    public AndroidManifest(InputStream AXMLStream) throws IOException {
        if (AXMLStream == null) {
            throw new IllegalArgumentException("AXMLStream == null");
        }

        try {
            printer = new AXMLPrinter(AXMLStream);

            Document document = DocumentHelper.parseText(printer.getXML());
            Element root = document.getRootElement();
            Namespace namespace = root.getNamespaceForURI(NS_ANDROID_URI);

            // packageName
            packageName = root.attributeValue("package");

            versionCode = root.attributeValue(new QName("versionCode",
                    namespace));
            versionName = root.attributeValue(new QName("versionName",
                    namespace));

            // permision
            permissions = getElements(root, "uses-permission", namespace,
                    "name", false);

            // check debuggable and allowBackup
            Element application = root.element("application");
            String debuggableValue = application.attributeValue(new QName(
                    "debuggable", namespace));
            if (debuggableValue != null && debuggableValue.equals("true")) {
                debuggable = true;
            }
            String allowBackupValue = application.attributeValue(new QName(
                    "allowBackup", namespace));
            if (allowBackupValue != null && allowBackupValue.equals("true")) {
                allowBakcup = true;
            }
            // mainActivity
            Set<String> x = new HashSet<String>();
            Set<String> y = new HashSet<String>();
            for (Iterator<Element> iter = root.element("application")
                    .elementIterator("activity"); iter.hasNext(); ) {
                Element activityElement = iter.next();
                List<Element> intentFilterList = activityElement
                        .elements("intent-filter");
                for (Element intentFilter : intentFilterList) {
                    List<Element> elements = intentFilter.elements();
                    for (Element element : elements) {
                        if (element.getName().equals("action")) {
                            if (element.attributeValue(
                                    new QName("name", namespace)).equals(
                                    "android.intent.action.MAIN")) {
                                x.add(activityElement.attributeValue(new QName(
                                        "name", namespace)));
                            }
                        } else if (element.getName().equals("category")) {
                            if (element.attributeValue(
                                    new QName("name", namespace)).equals(
                                    "android.intent.category.LAUNCHER")) {
                                y.add(activityElement.attributeValue(new QName(
                                        "name", namespace)));
                            }
                        }
                    }
                }
            }

            if (x.size() > 0 && y.size() > 0) {
                for (String name : x) {
                    if (y.contains(name)) {
                        mainActivity = convertFullName(name);
                        break;
                    }
                }
            } else {
                mainActivity = "undefined";
            }

            // acitivity
            activities = getElements(root.element("application"), "activity",
                    namespace, "name", true);

            exportedactivities = getExportedElements(root.element("application"), "activity",
                    namespace, true);

            // service
            services = getElements(root.element("application"), "service",
                    namespace, "name", true);

            exportedservices = getExportedElements(root.element("application"), "service",
                    namespace, true);

            // provider
            providers = getElements(root.element("application"), "provider",
                    namespace, "name", true);

            // receiver
            receivers = getElements(root.element("application"), "receiver",
                    namespace, "name", true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void writeXML(File outfile) {
        OutputStream out;
        try {
            out = new FileOutputStream(outfile);
            printer.dumpXML(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String toXMLString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        printer.dumpXML(byteArrayOutputStream);
        return byteArrayOutputStream.toString();
    }

    private List<String> getExportedElements(Element root, String tagName, Namespace namespace, boolean fullName) {
        List<String> list = new ArrayList<String>();
        for (Iterator<Element> iter = root.elementIterator(tagName); iter.hasNext(); ) {
            Element element = iter.next();
            String name = element.attributeValue(new QName("name", namespace));
            if (!"false".equals(element.attributeValue(new QName("exprted", namespace)))) {
                if (fullName) {
                    name = convertFullName(name);
                }
                list.add(name);
            }
        }
        return list;
    }

    private List<String> getElements(Element root, String tagName,
                                     Namespace namespace, String attribute, boolean fullName) {
        List<String> list = new ArrayList<String>();
        for (Iterator<Element> iter = root.elementIterator(tagName); iter.hasNext(); ) {
            Element element = iter.next();
            String name = element.attributeValue(new QName(attribute, namespace));
            if (fullName) {
                name = convertFullName(name);
            }
            list.add(name);
        }
        return list;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getMainActivity() {
        return mainActivity;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<String> getActivities() {
        return activities;
    }

    public List<String> getServices() {
        return services;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public List<String> getProviders() {
        return providers;
    }

    public List<String> getExportedactivities() {
        return exportedactivities;
    }

    public List<String> getExportedservices() {
        return exportedservices;
    }

    public boolean isDebuggable() {
        return debuggable;
    }

    public boolean isAllowBackup() {
        return allowBakcup;
    }

    // TODO: how to get full name path
    private String convertFullName(String name) {
        if (name.startsWith("."))
            return packageName + name;
        else if (!name.contains("."))
            return packageName + "." + name;
        return name;
    }
}
