package com.safecode.andcloud.dao;

import com.safecode.andcloud.model.APKInfo;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;


/**
 * 操作 apkinfo 数据库
 *
 * @author sumy
 */
@Repository
public class APKInfoDao extends BaseDao {

    public void saveOrUpdate(APKInfo apkInfo) {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(apkInfo);
    }

    public APKInfo findByMD5(String md5) {
        Query query = this.getSessionFactory().getCurrentSession().createQuery("select en from APKInfo en where en.md5 = ?1");
        query.setString("1", md5);
        return (APKInfo) query.uniqueResult();
    }

}
