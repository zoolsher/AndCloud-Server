package com.safecode.andcloud.worker;

import com.google.gson.Gson;
import com.safecode.andcloud.compoment.ScreenCastServer;
import com.safecode.andcloud.model.DeviceMap;
import com.safecode.andcloud.model.MirrorImage;
import com.safecode.andcloud.model.Project;
import com.safecode.andcloud.model.SimulatorDomain;
import com.safecode.andcloud.service.ADBService;
import com.safecode.andcloud.service.LibvirtService;
import com.safecode.andcloud.service.MirrorService;
import com.safecode.andcloud.service.ProjectService;
import com.safecode.andcloud.util.AAPTDumpLogInfoFinderUtil;
import com.safecode.andcloud.util.SpringContextUtil;
import com.safecode.andcloud.vo.EmulatorParameter;
import com.safecode.andcloud.vo.Work;
import com.safecode.andcloud.vo.message.CommandMessage;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.zeromq.ZMQ;

import java.io.File;
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
        this.projworkspace = new File(this.environment.getProperty("path.workspace")).toPath().resolve(project.getId() + "-" + work.getType());
        boolean dircreate = this.projworkspace.toFile().mkdirs();
        Path aaptlog = this.projworkspace.resolve("aaptdump.log");
        boolean result = adbService.aaptDumpApkInfo(environment.getProperty("path.aapt.version"), project.getFilename(), aaptlog.toString());
        AAPTDumpLogInfoFinderUtil aaptDumpLogInfoFinderUtil = new AAPTDumpLogInfoFinderUtil(aaptlog.toFile());
        if (project.getLogo() == null || project.getPackageName() == null) {
            if (result) {
                project.setLogo(aaptDumpLogInfoFinderUtil.getIcon(AAPTDumpLogInfoFinderUtil.ICON_APPLICATION));
                project.setPackageName(aaptDumpLogInfoFinderUtil.getPackages());
                projectService.saveOrUpdateProject(project);
            } else {
                logger.warn("[Worker] Can't get apk info for project-" + work.getProjectid() + " from user-" + work.getUid() + ". Exit.");
                return;
            }
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
                adbService.runAPK(this.emulatorIdentifier, aaptDumpLogInfoFinderUtil.getPackages(),
                        aaptDumpLogInfoFinderUtil.getLaunchActivity());

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
