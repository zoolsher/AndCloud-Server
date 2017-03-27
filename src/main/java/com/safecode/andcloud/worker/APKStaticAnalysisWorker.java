package com.safecode.andcloud.worker;

import com.google.gson.Gson;
import com.safecode.andcloud.model.APKInfo;
import com.safecode.andcloud.model.APKStaticAnalysisInfo;
import com.safecode.andcloud.model.Project;
import com.safecode.andcloud.service.APKInfoService;
import com.safecode.andcloud.util.AndroidManifest;
import com.safecode.andcloud.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 线程Worker，APK静态检测
 *
 * @author Sumy
 */
public class APKStaticAnalysisWorker extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(APKStaticAnalysisWorker.class);

    private APKInfoService apkInfoService;
    private Project project;

    public APKStaticAnalysisWorker(Project project) {
        this.apkInfoService = SpringContextUtil.getBean(APKInfoService.class);
        this.project = project;
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        ArrayList<String> fileList = new ArrayList<>();
        APKStaticAnalysisInfo info = new APKStaticAnalysisInfo();
        APKInfo apkinfo = project.getApkInfo();

        ArrayList<String> metainffile = new ArrayList<>();

        try {
            ZipFile apkFile = new ZipFile(project.getFilename());
            Enumeration<? extends ZipEntry> entries = apkFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                if (!zipEntry.isDirectory()) {
                    fileList.add(zipEntry.getName());
                    if (zipEntry.getName().startsWith("META-INF/")) {
                        metainffile.add(zipEntry.getName());
                    }
                }
            }

            // AndroidManifest 分析
            AndroidManifest androidManifest = new AndroidManifest(
                    apkFile.getInputStream(apkFile.getEntry("AndroidManifest.xml")));

            info.setActivitiesNum(androidManifest.getActivities().size());
            info.setExportedActivitiesNum(androidManifest.getExportedactivities().size());
            info.setActivities(gson.toJson(androidManifest.getActivities()));
            info.setServicesNum(androidManifest.getServices().size());
            info.setExportedServicesNum(androidManifest.getExportedservices().size());
            info.setServices(gson.toJson(androidManifest.getServices()));
            info.setProvidersNum(androidManifest.getProviders().size());
            info.setExportedProvidersNum(androidManifest.getProviders().size());
            info.setProviders(gson.toJson(androidManifest.getProviders()));
            info.setReceiversNum(androidManifest.getReceivers().size());
            info.setExportedReceiversNum(androidManifest.getReceivers().size());
            info.setReceivers(gson.toJson(androidManifest.getReceivers()));
            info.setPermissions(gson.toJson(androidManifest.getPermissions()));
            info.setManifest(androidManifest.toXMLString());
            info.setFiles(gson.toJson(fileList));

            // 证书签名分析
            String certfile = "";
            String certissued = "";
            for (String file : metainffile) {
                if (file.endsWith("CERT.RSA")) {
                    certfile = file;
                    break;
                } else if (file.toLowerCase().endsWith(".rsa")) {
                    certfile = file;
                    break;
                } else if (file.toLowerCase().endsWith(".dsa")) {
                    certfile = file;
                    break;
                } else {
                    certissued = "missing";
                }
            }
            StringBuilder certOutput = new StringBuilder();
            if (!"".equals(certfile)) {
                try {
                    InputStream certInputStream = apkFile.getInputStream(apkFile.getEntry(certfile));
                    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                    Collection<? extends Certificate> collection = certificateFactory.generateCertificates(certInputStream);
                    for (Certificate certificate : collection) {
                        certOutput.append(certificate.toString().replace(certificate.getPublicKey().toString(), ""))
                                .append("\n");
                    }
                    certissued = "good";
                } catch (CertificateException e) {
                    certissued = "broken";
                    e.printStackTrace();
                }
                if (certOutput.indexOf("Issuer: CN=Android Debug") != -1 || certOutput.indexOf("Subject: CN=Android Debug") != -1) {
                    certissued = "bad";
                }
//                if (certOutput.indexOf("[SHA1withRSA]") != -1) {
//                    certissued = "bad hash";
//                }
            }
            info.setSignerCertificate(certOutput.toString());
            info.setCertIssued(certissued);

            apkinfo.setStaticAnalysisInfo(info);
            info.setApkInfo(apkinfo);

            apkInfoService.saveOrUpdateAPKStaticAnalysisInfo(info);
            apkInfoService.saveOrUpdateAPKInfo(apkinfo);
        } catch (IOException e) {
            logger.error("[APKStaticAnalysis]Can't open apkfile for Project-" + project.getId(), e);
        }
    }
}
