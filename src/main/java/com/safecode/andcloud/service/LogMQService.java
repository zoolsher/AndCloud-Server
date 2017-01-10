package com.safecode.andcloud.service;


import com.google.gson.Gson;
import com.safecode.andcloud.vo.message.LogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.zeromq.ZMQ.*;

/**
 * Created by zoolsher on 2016/12/26.
 *
 * @author zoolsher
 */
@Service
public class LogMQService {
    private final static Logger logger = LoggerFactory.getLogger(LogMQService.class);

    @Autowired
    @Qualifier("logmq")
    private Socket _mq;

    // Thread IndexOutOfBoundException, need synchronized
    public synchronized void sendDeviceLog(String id, String log) {
        try {
            Gson g = new Gson();
            LogMessage logMessage = new LogMessage();
            logMessage.setId(id);
            logMessage.setLog(log);
            String message = g.toJson(logMessage);
            _mq.send(message);
        } catch (Exception e) {
            logger.error("An Error occur while send log to device-" + id + " log " + log, e);
        }
    }

}
