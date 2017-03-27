package com.safecode.andcloud.dao;

import com.safecode.andcloud.model.AndroidPermissions;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 * 操作AndroidPermissions数据库
 *
 * @author Sumy
 */
@Repository
public class AndroidPermissionsDao extends BaseDao {

    public void clear() {
        Query query = this.getSessionFactory().getCurrentSession().createQuery("delete from AndroidPermissions");
        query.executeUpdate();
    }

    public void saveOrUpdate(AndroidPermissions permissions) {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(permissions);
    }

}
