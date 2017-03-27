package com.safecode.andcloud.service;

import com.safecode.andcloud.dao.APKInfoDao;
import com.safecode.andcloud.dao.APKStaticAnalysisInfoDao;
import com.safecode.andcloud.dao.AndroidPermissionsDao;
import com.safecode.andcloud.model.APKInfo;
import com.safecode.andcloud.model.APKStaticAnalysisInfo;
import com.safecode.andcloud.model.AndroidPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * APKInfo相关
 *
 * @author Sumy
 */
@Service
@Transactional
public class APKInfoService {

    @Autowired
    private APKInfoDao apkInfoDao;

    @Autowired
    private AndroidPermissionsDao androidPermissionsDao;

    @Autowired
    private APKStaticAnalysisInfoDao staticAnalysisInfoDao;

    public APKInfo getAPKInfoById(int id) {
        return apkInfoDao.findById(id);
    }

    public APKInfo getAPKInfoByAPKMd5(String md5) {
        return apkInfoDao.findByMD5(md5);
    }

    public void saveOrUpdateAPKInfo(APKInfo apkInfo) {
        apkInfoDao.saveOrUpdate(apkInfo);
    }

    public void saveOrUpdateAPKStaticAnalysisInfo(APKStaticAnalysisInfo apkStaticAnalysisInfo) {
        staticAnalysisInfoDao.saveOrUpdate(apkStaticAnalysisInfo);
    }

    public void clearAndroidPermissions() {
        androidPermissionsDao.clear();
    }

    public void saveAndroidPermissions(Collection<AndroidPermissions> permissions) {
        for (AndroidPermissions permission : permissions) {
            androidPermissionsDao.saveOrUpdate(permission);
        }
    }
}
