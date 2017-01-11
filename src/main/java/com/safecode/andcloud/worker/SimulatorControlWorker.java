package com.safecode.andcloud.worker;

import com.google.gson.Gson;
import com.safecode.andcloud.compoment.ScreenCastServer;
import com.safecode.andcloud.model.*;
import com.safecode.andcloud.service.ADBService;
import com.safecode.andcloud.service.LibvirtService;
import com.safecode.andcloud.service.MirrorService;
import com.safecode.andcloud.service.ProjectService;
import com.safecode.andcloud.util.AAPTDumpLogInfoFinderUtil;
import com.safecode.andcloud.util.APKFileUtil;
import com.safecode.andcloud.util.SpringContextUtil;
import com.safecode.andcloud.vo.EmulatorParameter;
import com.safecode.andcloud.vo.Work;
import com.safecode.andcloud.vo.message.CommandMessage;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.zeromq.ZMQ;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;


/**
 * 控制虚拟机，创建线程，或执行想要执行的操作
 *
 * @author sumy
 * @author zoolsher
 * @author sharp
 */
public class SimulatorControlWorker implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(SimulatorControlWorker.class);

    private ProjectService projectService;
    private MirrorService mirrorService;
    private LibvirtService libvirtService;
    private ScreenCastServer screenCastServer;
    private ADBService adbService;
    private Environment environment;

    private Work work;
    private String emulatorIdentifier;
    private Path projworkspace;

    public SimulatorControlWorker(Work work) {
        this.work = work;
        this.projectService = SpringContextUtil.getBean(ProjectService.class);
        this.mirrorService = SpringContextUtil.getBean(MirrorService.class);
        this.libvirtService = SpringContextUtil.getBean(LibvirtService.class);
        this.adbService = SpringContextUtil.getBean(ADBService.class);
        this.environment = SpringContextUtil.getBean(Environment.class);
        this.screenCastServer = SpringContextUtil.getBean(ScreenCastServer.class);
    }

    @Override
    public void run() {
        Project project = projectService.findProjectByUserIdAndProjectId(work.getUid(), work.getProjectid());
        if (project == null) {
            logger.info("[Worker] Can't found project-" + work.getProjectid() + " from user-" + work.getUid() + ". Exit.");
            return;
        }
        MirrorImage mirrorImage = project.getMirrorImage();
        String imagePath = libvirtService.createDeriveImageFromMasterImage(mirrorImage.getPath(), work.getUid().toString());
        if (imagePath.length() == 0) {
            logger.warn("[Worker] Can't create work image for project-" + work.getProjectid() + " from user-" + work.getUid() + ". Exit.");
        }
        SimulatorDomain simulatorDomain = mirrorService.newSimulatorDomain(work.getProjectid(),
                work.getUid(), work.getType(), imagePath, work.getTime());
        this.projworkspace = new File(this.environment.getProperty("path.workspace")).toPath().resolve(project.getId() + "-" + work.getType() + "-" + simulatorDomain.getUuid());
        boolean dircreate = this.projworkspace.toFile().mkdirs();

        // 复制apk文件到工作目录
        File infile = new File(project.getFilename());
        File simplefile = this.projworkspace.resolve("simple.apk").toFile();
        try {
            FileUtils.copyFile(infile, simplefile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 计算文件md5并插入apk数据库
        APKInfo apkInfo = null;
        try {
            FileInputStream fis = new FileInputStream(infile);
            byte[] filebytes = IOUtils.toByteArray(fis);
            String md5 = DigestUtils.md5Hex(filebytes);
            apkInfo = projectService.getAPKInfoByAPKMd5(md5);
            // 数据库中不存在APKInfo，则重新计算并插入
            if (apkInfo == null) {
                Path aaptlog = this.projworkspace.resolve("aaptdump.log");
                boolean result = adbService.aaptDumpApkInfo(environment.getProperty("path.aapt.version"), project.getFilename(), aaptlog.toString());
                AAPTDumpLogInfoFinderUtil aaptDumpLogInfoFinderUtil = new AAPTDumpLogInfoFinderUtil(aaptlog.toFile());
                String sha1 = DigestUtils.sha1Hex(filebytes);
                String sha256 = DigestUtils.sha256Hex(filebytes);
                ZipFile apkzipfile = new ZipFile(infile);

                apkInfo = new APKInfo();
                apkInfo.setSize(filebytes.length);
                apkInfo.setPackagename(aaptDumpLogInfoFinderUtil.getPackages());
                apkInfo.setMainactivity(aaptDumpLogInfoFinderUtil.getLaunchActivity());
                apkInfo.setVersioncode(aaptDumpLogInfoFinderUtil.getVersionCode());
                apkInfo.setVersionname(aaptDumpLogInfoFinderUtil.getVersionName());
                apkInfo.setMaxsdk(null);
                apkInfo.setMinsdk(aaptDumpLogInfoFinderUtil.getSdkversion());
                apkInfo.setTargetsdk(aaptDumpLogInfoFinderUtil.getTargetsdkversion());
                apkInfo.setMd5(md5);
                apkInfo.setSha1(sha1);
                apkInfo.setSha256(sha256);
                apkInfo.setLabel(aaptDumpLogInfoFinderUtil.getLabel(AAPTDumpLogInfoFinderUtil.LABEL_APPLICATION));
                apkInfo.setIcon(aaptDumpLogInfoFinderUtil.getIcon(AAPTDumpLogInfoFinderUtil.ICON_APPLICATION));

                ZipArchiveEntry iconentry = apkzipfile.getEntry(apkInfo.getIcon());
                String subfix = apkInfo.getIcon().substring(apkInfo.getIcon().lastIndexOf(".") + 1);
                String iconbase64 = APKFileUtil.imgToBase64Code(subfix, apkzipfile.getInputStream(iconentry));
                apkInfo.setIconimg(iconbase64);

                projectService.saveOrUpdateAPKInfo(apkInfo);

                project.setApkInfo(apkInfo);
                projectService.saveOrUpdateProject(project);
            }
        } catch (FileNotFoundException e) {
            logger.error("[Worker]Can't find source APK for project-" + project.getId() + ". Exit.", e);
            return;
        } catch (IOException e) {
            logger.error("[Worker]Can't process APK file for project-" + project.getId() + ". Exit", e);
            return;
        }

        DeviceMap deviceMap = mirrorService.newDeviceMap(project, simulatorDomain, work.getType());
        EmulatorParameter parameter = new EmulatorParameter(mirrorImage.getWidth(), mirrorImage.getHeight(),
                mirrorImage.getWmwidth(), mirrorImage.getWmheight(), 32767, 32767,
                "/dev/input/event1");
        try {

            ZMQ.Context ctx = ZMQ.context(1);
            ZMQ.Socket socket = ctx.socket(ZMQ.SUB);
            String endpoint = environment.getRequiredProperty("mq.command.endpoint");
            socket.connect(endpoint);
            socket.subscribe("".getBytes());

            logger.info("[Worker] Define and Start Simulator");
            libvirtService.defineDomain(simulatorDomain);
            libvirtService.startDomainByDomainName(simulatorDomain.getName());
            // 等待 60 秒 系统启动
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 获取IP地址
            String ip = libvirtService.getIPAddressByMacAddress(simulatorDomain.getMac());
            logger.info("SIM " + simulatorDomain.getName() + " IP " + ip);
            screenCastServer.register(ip, simulatorDomain.getId());
            if (ip == null || ip.equals("")) {
                // 未获取到 IP 等待 1 秒后重新获取
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ip = libvirtService.getIPAddressByMacAddress(simulatorDomain.getMac());
                if (ip == null || ip.equals("")) {
                    logger.error("[Worker]Cant got IP for SIM-" + simulatorDomain.getName() + ". Exit.");
                }
            } else {
                this.emulatorIdentifier = ip + ":5555";
                adbService.connect(this.emulatorIdentifier);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LogcatWorker logcatWorker = new LogcatWorker(this.projworkspace.resolve("logcat.log").toString(),
                        this.emulatorIdentifier, simulatorDomain.getId() + "");
                logcatWorker.start();
                adbService.startScreenCastService(this.emulatorIdentifier, environment.getProperty("screencast.server.address"),
                        environment.getProperty("screencast.server.port"));
                ScreenTouchWorker touchWorker = new ScreenTouchWorker(this.emulatorIdentifier, simulatorDomain.getId(), parameter);
                touchWorker.start();

                // 安装 APK
                adbService.installAPk(this.emulatorIdentifier, project.getFilename());
                adbService.runAPK(this.emulatorIdentifier, apkInfo.getPackagename(), apkInfo.getMainactivity());

                Gson gson = new Gson();
                while (true) {
                    String msg = new String(socket.recv());
                    CommandMessage message = gson.fromJson(msg, CommandMessage.class);
                    if ((simulatorDomain.getId() + "").equals(message.getId())) {
                        if (CommandMessage.COMMAND_CLOSE.equals(message.getCommand())) {
                            logger.debug("Time run out. Closeing...");
                            logcatWorker.stopme();
                            touchWorker.stopme();
                            break;
                        }
                    }
                }
            }
            screenCastServer.unreigster(ip);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            libvirtService.stopDomainByDomainName(simulatorDomain.getName());
            libvirtService.undefineDomainByDomainName(simulatorDomain.getName());
            mirrorService.deleteSimulatorDomain(simulatorDomain);

        } catch (LibvirtException e) {
            logger.error("Libvirt operater failed.", e);
        }
    }
}
