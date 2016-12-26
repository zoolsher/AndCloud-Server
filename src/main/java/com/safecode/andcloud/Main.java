package com.safecode.andcloud;

import com.safecode.andcloud.service.LibvirtService;
import com.safecode.andcloud.util.DomainDefineXMLUtil;
import com.safecode.andcloud.worker.MessageReciverWorker;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

        LibvirtService libvirtService = ctx.getBean(LibvirtService.class);
        try {
            libvirtService.listAllDomain();
        } catch (LibvirtException e) {
            e.printStackTrace();
        }
    }

}