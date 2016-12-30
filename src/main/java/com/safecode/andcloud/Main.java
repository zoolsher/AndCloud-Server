package com.safecode.andcloud;

import com.safecode.andcloud.compoment.ControlACWebSocketServer;
import com.safecode.andcloud.compoment.LogACWebSocketServer;
import com.safecode.andcloud.compoment.ScreenCastServer;
import com.safecode.andcloud.util.DomainDefineXMLUtil;
import com.safecode.andcloud.worker.MessageReciverWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * Server com.safecode.andcloud.Main Class.
 *
 * @author zoolsher
 * @author Sumy
 */
public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Server Start...");
        logger.info("Loading Spring Context");
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.safecode.andcloud");
        logger.info("Server initialization finished.");

        logger.info("Load domaindefine XML file.");
        logger.debug(DomainDefineXMLUtil.template);

        logger.info("Start MessageReciver");
        MessageReciverWorker worker = ctx.getBean(MessageReciverWorker.class);
        worker.start();

        logger.info("Start Log WebSocket Server");
        LogACWebSocketServer logACWebSocketServer = ctx.getBean(LogACWebSocketServer.class);
        logACWebSocketServer.start();

        logger.info("Start Control WebSocket Server");
        ControlACWebSocketServer controlACWebSocketServer = ctx.getBean(ControlACWebSocketServer.class);
        controlACWebSocketServer.start();

        logger.info("Start ScreenCastServer");
        ScreenCastServer screenCastServer = ctx.getBean(ScreenCastServer.class);
        try {
            screenCastServer.start();
        } catch (IOException e) {
            logger.error("ScreenCastServer Start Errror. Exit.", e);
            System.exit(1);
        }
    }

}