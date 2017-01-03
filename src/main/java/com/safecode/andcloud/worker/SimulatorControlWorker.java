package com.safecode.andcloud.worker;

import com.google.gson.Gson;
import com.safecode.andcloud.model.DeviceMap;
import com.safecode.andcloud.model.Project;
import com.safecode.andcloud.model.SimulatorDomain;
import com.safecode.andcloud.service.ADBService;
import com.safecode.andcloud.service.LibvirtService;
import com.safecode.andcloud.service.MirrorService;
import com.safecode.andcloud.service.ProjectService;
import com.safecode.andcloud.util.SpringContextUtil;
import com.safecode.andcloud.vo.Work;
import com.safecode.andcloud.vo.message.CommandMessage;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.zeromq.ZMQ;

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
    private ADBService adbService;
    private Environment environment;

    private Work work;
    private String emulatorIdentifier;

    public SimulatorControlWorker(Work work) {
        this.work = work;
        this.projectService = SpringContextUtil.getBean(ProjectService.class);
        this.mirrorService = SpringContextUtil.getBean(MirrorService.class);
        this.libvirtService = SpringContextUtil.getBean(LibvirtService.class);
        this.adbService = SpringContextUtil.getBean(ADBService.class);
        this.environment = SpringContextUtil.getBean(Environment.class);
    }

    @Override
    public void run() {
        Project project = projectService.findProjectByUserIdAndProjectId(work.getUid(), work.getProjectid());
        if (project == null) {
            logger.info("[Worker] Can't found project-" + work.getProjectid() + " from user-" + work.getUid() + ". Exit.");
            return;
        }
        String imagePath = libvirtService.createDeriveImageFromMasterImage("/var/lib/libvirt/images/branch-4.4-rc4-v1.img", work.getUid().toString());
        if (imagePath.length() == 0) {
            logger.warn("[Worker] Can't create work image for project-" + work.getProjectid() + " from user-" + work.getUid() + ". Exit.");
        }
        SimulatorDomain simulatorDomain = mirrorService.newSimulatorDomain(work.getProjectid(),
                work.getUid(), work.getType(), imagePath);
        DeviceMap deviceMap = mirrorService.newDeviceMap(project, simulatorDomain, work.getType());
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
                LogcatWorker logcatWorker = new LogcatWorker(simulatorDomain.getName() + ".txt", ip, simulatorDomain.getId() + "");
                logcatWorker.start();
                // TODO 安装apk
                
                Gson gson = new Gson();
                while (true)
                {
                    String msg = new String(socket.recv());
                    CommandMessage message = gson.fromJson(msg, CommandMessage.class);
                    if((simulatorDomain.getId()+"").equals(message.getId())){
                        if (CommandMessage.COMMAND_CLOSE.equals(message.getCommand())){
                            logcatWorker.stopme();
                            break;
                        }
                    }
                }
            }
            libvirtService.stopDomainByDomainName(simulatorDomain.getName());
            libvirtService.undefineDomainByDomainName(simulatorDomain.getName());
            mirrorService.deleteSimulatorDomain(simulatorDomain);

        } catch (LibvirtException e) {
            logger.error("Libvirt operater failed.", e);
        }
        logger.info(work.getProjectid().toString());
        System.out.println(work.getProjectid());
        System.out.println(work.getType());
        System.out.println(work.getUid());
    }
}
