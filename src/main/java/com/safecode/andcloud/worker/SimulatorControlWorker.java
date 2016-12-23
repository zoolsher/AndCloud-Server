package com.safecode.andcloud.worker;

import com.safecode.andcloud.model.Project;
import com.safecode.andcloud.model.SimulatorDomain;
import com.safecode.andcloud.service.ADBService;
import com.safecode.andcloud.service.LibvirtService;
import com.safecode.andcloud.service.MirrorService;
import com.safecode.andcloud.service.ProjectService;
import com.safecode.andcloud.util.SpringContextUtil;
import com.safecode.andcloud.vo.Work;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 控制虚拟机，创建线程，或执行想要执行的操作
 */
public class SimulatorControlWorker implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(SimulatorControlWorker.class);

    private ProjectService projectService;
    private MirrorService mirrorService;
    private LibvirtService libvirtService;
    private ADBService adbService;

    private Work work;

    public SimulatorControlWorker(Work work) {
        this.work = work;
        this.projectService = SpringContextUtil.getBean(ProjectService.class);
        this.mirrorService = SpringContextUtil.getBean(MirrorService.class);
        this.libvirtService = SpringContextUtil.getBean(LibvirtService.class);
        this.adbService = SpringContextUtil.getBean(ADBService.class);
    }

    @Override
    public void run() {
        Project project = projectService.findProjectByUserIdAndProjectId(work.getUid(), work.getProjectid());
        if (project == null) {
            logger.info("[Worker] Can't found project-" + work.getProjectid() + " from user-" + work.getUid() + ". Exit.");
            return;
        }
        SimulatorDomain simulatorDomain = mirrorService.newSimulatorDomain(work.getProjectid(),
                work.getUid(), work.getType(), "/var/lib/libvirt/images/android2.img");
        try {
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
                adbService.connectSIMByIP(ip);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LogcatWorker logcatWorker = new LogcatWorker(simulatorDomain.getName() + ".txt", ip);
                logcatWorker.start();
                // 写死的 1 分钟检测时间
                // TODO 检测时间配置
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logcatWorker.stopme();
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
