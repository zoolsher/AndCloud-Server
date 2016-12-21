package com.safecode.andcloud.service;

import com.safecode.andcloud.dao.ProjectDao;
import com.safecode.andcloud.model.Project;
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

    public Project findProjectByUserIdAndProjectId(Integer userid, Integer projectid) {
        return projectDao.findByUserIdAndProjectId(userid, projectid);
    }

}
