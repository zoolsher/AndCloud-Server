package com.safecode.andcloud.service;

import com.safecode.andcloud.model.SimulatorDomain;
import com.safecode.andcloud.util.DomainDefineXMLUtil;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

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

    public void defineDomain(SimulatorDomain simulatorDomain) throws LibvirtException {
        DomainDefineXMLUtil.DomainAttr domainAttr = new DomainDefineXMLUtil.DomainAttr();
        domainAttr.domainName = simulatorDomain.getName();
        domainAttr.macAddress = simulatorDomain.getMac();
        domainAttr.imagePath = simulatorDomain.getImagepath();
        domainAttr.uuid = simulatorDomain.getUuid();
        String defineXML = DomainDefineXMLUtil.getDomainDefineXMLString(domainAttr);
        Domain domain = conn.domainDefineXML(defineXML);
//        return domain;
    }

    public void undefineDomainByDomainName(String domainName) throws LibvirtException {
        Domain domain = conn.domainLookupByName(domainName);
        domain.undefine();

    }

    public void startDomainByDomainName(String domainName) throws LibvirtException {
        Domain domain = conn.domainLookupByName(domainName);
        domain.create();
    }

    public void stopDomainByDomainName(String domainName) throws LibvirtException {
        Domain domain = conn.domainLookupByName(domainName);
        domain.destroy();
    }

    public String getIPAddressByMacAddress(String macAddress) {
        String command = "arp -n";
        String ip = "";
        try {
            Process process = Runtime.getRuntime().exec(command);
            InputStreamReader instream = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(instream);
            process.waitFor();
            String line = null;
            while ((line = input.readLine()) != null) {
                if (line.contains(macAddress)) {
                    ip = line.trim().split(" ")[0];
                    break;
                }
            }
        } catch (IOException e) {
            logger.error("Get IP Address Error.", e);
        } catch (InterruptedException e) {
            logger.error("Get IP Address Error.", e);
        }
        return ip;
    }
}
