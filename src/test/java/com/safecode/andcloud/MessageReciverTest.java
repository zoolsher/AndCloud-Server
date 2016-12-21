package com.safecode.andcloud;

import com.google.gson.Gson;
import com.safecode.andcloud.vo.message.NewWorkMessage;
import com.safecode.andcloud.worker.MessageReciverWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zeromq.ZMQ;

/**
 * 一个消息发送者
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {com.safecode.andcloud.configuration.ApplicationContext.class,
        com.safecode.andcloud.configuration.HibernateConfiguration.class})
public class MessageReciverTest {

    @Autowired
    private Environment environment;

    @Autowired
    private MessageReciverWorker worker;

    @Test
    public void sendMessageEverySecond() {
        worker.start();

        final ZMQ.Context context = ZMQ.context(1);//number of ioThreads
        final ZMQ.Socket req = context.socket(ZMQ.REQ);
        req.connect(environment.getProperty("mq.webpage.endpoint"));
        Gson gson = new Gson();
        int id = 0;
        while (true) {
            NewWorkMessage message = new NewWorkMessage();
            message.setProjectid(id);
            message.setType(NewWorkMessage.TYPE_AUTOMATIC);
            message.setUid((int) (Math.random() * Integer.MAX_VALUE));
            String jsonmsg = gson.toJson(message);
            req.send(jsonmsg.getBytes());
            id++;
            byte[] request = req.recv();
            System.out.println(new String(request));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (id > 10) break;
        }
    }

}
