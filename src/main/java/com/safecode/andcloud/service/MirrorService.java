package com.safecode.andcloud.service;

import com.safecode.andcloud.dao.DeviceMapDao;
import com.safecode.andcloud.dao.SimulatorDomainDao;
import com.safecode.andcloud.model.DeviceMap;
import com.safecode.andcloud.model.Project;
import com.safecode.andcloud.model.SimulatorDomain;
import com.safecode.andcloud.util.DomainAttrUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 操作虚拟机镜像的相关服务
 *
 * @author sumy
 * @author zoolsher
 */
@Service
@Transactional
public class MirrorService {

    @Autowired
    private SimulatorDomainDao simulatorDomainDao;
    @Autowired
    private DeviceMapDao deviceMapDao;


    public SimulatorDomain newSimulatorDomain(int projid, int userid, int type, String imagePath) {
        SimulatorDomain domain = new SimulatorDomain();
        domain.setName(projid + "-" + userid + "-" + type + "-" + new DateTime().getSecondOfMinute()); // 命名方式 项目编号-用户编号-检测方式-当前秒
        domain.setCreatetime(new DateTime());
        domain.setImagepath(imagePath);
        domain.setIsdelete(false);
        domain.setUuid(DomainAttrUtil.generateUUID());
        domain.setMac(DomainAttrUtil.generateMACAddress());
        simulatorDomainDao.saveOrUpdate(domain);
        return domain;
    }

    public DeviceMap newDeviceMap(Project project,SimulatorDomain simulatorDomain,Integer type){
        DeviceMap deviceMap = new DeviceMap();
        deviceMap.setProject(project);
        deviceMap.setType(type);
        deviceMap.setSimulatorDomain(simulatorDomain);
        deviceMapDao.saveOrUpdate(deviceMap);
        return deviceMap;
    }

    public void deleteSimulatorDomain(SimulatorDomain simulatorDomain) {
        simulatorDomain.setIsdelete(true);
        simulatorDomainDao.saveOrUpdate(simulatorDomain);
    }

    public List<SimulatorDomain> findUndeleteSimulator()
    {
        return simulatorDomainDao.findByIsDelete(false);
    }

}
