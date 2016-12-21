package com.safecode.andcloud.worker;

import com.safecode.andcloud.vo.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 控制虚拟机，创建线程，或执行想要执行的操作
 */
public class SimulatorControlWorker implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(SimulatorControlWorker.class);

    private Work work;

    public SimulatorControlWorker(Work work) {
        this.work = work;
    }

    @Override
    public void run() {
        // TODO 从数据库查询任务详情，创建虚拟机，启动新线程获取模拟器数据
        logger.info(work.getProjectid().toString());
        System.out.println(work.getProjectid());
        System.out.println(work.getType());
        System.out.println(work.getUid());
    }
}
