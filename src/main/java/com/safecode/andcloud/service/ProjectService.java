package com.safecode.andcloud.service;

import com.safecode.andcloud.dao.DeviceMapDao;
import com.safecode.andcloud.dao.ProjectDao;
import com.safecode.andcloud.dao.TokenDao;
import com.safecode.andcloud.model.DeviceMap;
import com.safecode.andcloud.model.Project;
import com.safecode.andcloud.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工程逻辑
 *
 * @author Sumy
 */
@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    private DeviceMapDao deviceMapDao;

    public void saveOrUpdateProject(Project project) {
        projectDao.saveOrUpdate(project);
    }

    public Project findProjectByUserIdAndProjectId(Integer userid, Integer projectid) {
        return projectDao.findByUserIdAndProjectId(userid, projectid);
    }

    public Token getTokenByName(String tokenName) {
        return tokenDao.getInfoViaToken(tokenName);
    }

    public DeviceMap getDeviceMapByProjectAndType(Project project, int type) {
//        return deviceMapDao.getByProjectAndType(project, type);
        return deviceMapDao.getLastByProjectAndType(project, type);
    }

    public Integer getDeviceIdByProjectAndType(Project project, int type) {
        DeviceMap deviceMap = this.getDeviceMapByProjectAndType(project, type);
        if (null == deviceMap) {
            return null;
        } else {
            return deviceMap.getSimulatorDomain().getId();
        }
    }
}
