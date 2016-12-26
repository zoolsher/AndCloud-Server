package com.safecode.andcloud.service;

import com.safecode.andcloud.dao.SimulatorDomainDao;
import com.safecode.andcloud.model.SimulatorDomain;
import com.safecode.andcloud.util.DomainAttrUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 操作虚拟机镜像的相关服务
 *
 * @author sumy
 */
@Service
@Transactional
public class MirrorService {

    @Autowired
    private SimulatorDomainDao simulatorDomainDao;

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

    public void deleteSimulatorDomain(SimulatorDomain simulatorDomain) {
        simulatorDomain.setIsdelete(true);
        simulatorDomainDao.saveOrUpdate(simulatorDomain);
    }

}
