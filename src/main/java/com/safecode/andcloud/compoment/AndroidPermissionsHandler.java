package com.safecode.andcloud.compoment;

import com.safecode.andcloud.model.AndroidPermissions;
import com.safecode.andcloud.service.APKInfoService;
import com.safecode.andcloud.vo.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * 将AndroidManifest Permissions数据读入内存
 *
 * @author Sumy
 */
@Component
public class AndroidPermissionsHandler {
    private static final Logger logger = LoggerFactory.getLogger(AndroidPermissionsHandler.class);

    private List<Permission> androidPermissions;
    private List<AndroidPermissions> androidPermissionsList;

    @Autowired
    private APKInfoService apkInfoService;

    public AndroidPermissionsHandler() {
        androidPermissions = new ArrayList<>();
        androidPermissionsList = new ArrayList<>();
    }

    @PostConstruct
    public void initAndroidPermissionDatabase() {
        logger.info("Loading AndroidPermission to database.");

        Resource resource = new ClassPathResource("androidpermissions.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(resource.getInputStream());

            NodeList permissionList = doc.getElementsByTagName("permission");
            for (int i = 0; i < permissionList.getLength(); i++) {
                Node perm = permissionList.item(i);
                Element elem = (Element) perm;
                Permission permission = new Permission();
                permission.id = Integer.parseInt(elem.getAttribute("id"));
                permission.name = elem.getAttribute("name");
                permission.group = elem.getAttribute("group");
                permission.level = elem.getAttribute("level");
                permission.flags = elem.getAttribute("flags");
                permission.label = elem.getAttribute("label");
                permission.description = elem.getAttribute("description");
                logger.debug("Permission Info -" + permission);
                androidPermissions.add(permission);
            }

            apkInfoService.clearAndroidPermissions();
            for (Permission permission : androidPermissions) {
                AndroidPermissions androidPermissions = new AndroidPermissions();
                androidPermissions.setId(permission.getId());
                androidPermissions.setDescription(permission.getDescription());
                androidPermissions.setInfo(permission.getLabel());
                androidPermissions.setName(permission.getName());
                androidPermissions.setStatus(permission.getLevel());
                androidPermissionsList.add(androidPermissions);
            }

            apkInfoService.saveAndroidPermissions(androidPermissionsList);
        } catch (Exception e) {
            logger.error("An error occur when load android permission list.", e);
        }
    }

}
