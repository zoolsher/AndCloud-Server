package com.safecode.andcloud.dao;

import com.safecode.andcloud.model.APKStaticAnalysisInfo;
import org.springframework.stereotype.Repository;

/**
 * 操作 apkstaticanalysisinfo 数据库
 *
 * @author sumy
 */
@Repository
public class APKStaticAnalysisInfoDao extends BaseDao {

    public void saveOrUpdate(APKStaticAnalysisInfo info) {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(info);
    }
}
