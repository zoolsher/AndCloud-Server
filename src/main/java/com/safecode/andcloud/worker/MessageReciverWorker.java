package com.safecode.andcloud.worker;

import com.google.gson.Gson;
import com.safecode.andcloud.vo.Work;
import com.safecode.andcloud.vo.message.NewWorkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.zeromq.ZMQ;

/**
 * 消息队列接受者
 *
 * @author sumy
 */

public class MessageReciverWorker extends Thread {

    private final static Logger logger = LoggerFactory.getLogger(MessageReciverWorker.class);

    @Autowired
    private Environment environment;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Override
    public void run() {
        String endpoint = environment.getProperty("mq.webpage.endpoint");
        final ZMQ.Context context = ZMQ.context(1);
        final ZMQ.Socket rep = context.socket(ZMQ.REP);
        rep.bind(endpoint);
        logger.info("Work Reciver bind on: " + endpoint);

        Gson gson = new Gson();

        while (!Thread.currentThread().isInterrupted()) {
            byte[] request = rep.recv();
            String message = new String(request);
            logger.debug("Recived data from webpage: " + message);
            NewWorkMessage workMessage = gson.fromJson(message, NewWorkMessage.class);
            if (workMessage != null && workMessage.isvalid()) {
                executor.execute(new SimulatorControlWorker(new Work(workMessage)));
                rep.send("OK");
            } else {
                rep.send("ERROR");
            }
        }
    }
}
