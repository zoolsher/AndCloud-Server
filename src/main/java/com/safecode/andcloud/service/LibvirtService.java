package com.safecode.andcloud.service;

import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Libvirt 虚拟机操作
 *
 * @author Sumy
 */
@Service
public class LibvirtService {

    private final static Logger logger = LoggerFactory.getLogger(LibvirtService.class);

    @Autowired
    private Connect conn;

    public void listAllDomain() throws LibvirtException {
        logger.info("Start list domains...");
        for (String dom : conn.listDefinedDomains()) {
            Domain domain = conn.domainLookupByName(dom);
            System.out.println(domain.getID() + ":" + domain.getName());
        }
        System.out.println("---------------------");
        for (int id : conn.listDomains()) {
            Domain dom = conn.domainLookupByID(id);
            System.out.println(dom.getID() + ":" + dom.getName());
        }
    }

}
