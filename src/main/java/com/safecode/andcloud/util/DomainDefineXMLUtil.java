package com.safecode.andcloud.util;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * Domain定义文件生成工具
 *
 * @author sumy
 */
public class DomainDefineXMLUtil {

    private final static Logger logger = LoggerFactory.getLogger(DomainDefineXMLUtil.class);
    public static final String template;

    static {
        StringBuilder buf = new StringBuilder();
        BufferedReader reader = null;
        try {
            Resource resource = SpringContextUtil.getBean(Resource.class, "domaindefine.xml");
            reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buf.append(line);
            }
        } catch (FileNotFoundException e) {
            logger.error("Domain define file not found!", e);
        } catch (IOException e) {
            logger.error("An error occur while loading Domain define file.", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("An error occur while close reader.", e);
                }
            }
        }
        template = buf.toString();
    }

    public static class DomainAttr {
        public String domainName;
        public String uuid;
        public String imagePath;
        public String macAddress;
    }

    public static String getDomainDefineXMLString(DomainAttr attr) {
        return template.replace("{domainName}", attr.domainName)
                .replace("{uuid}", attr.uuid)
                .replace("{imagePath}", attr.imagePath)
                .replace("{macAddress}", attr.macAddress);
    }

    public static void main(String[] args) {
        System.out.println(DomainDefineXMLUtil.template);
        DomainAttr attr = new DomainAttr();
        attr.domainName = "Android " + new DateTime().toString();
        attr.uuid = DomainAttrUtil.generateUUID();
        attr.imagePath = "testPath";
        attr.macAddress = DomainAttrUtil.generateMACAddress();
        System.out.println(getDomainDefineXMLString(attr));
    }
}
