package com.safecode.andcloud.dao;

import com.safecode.andcloud.model.DeviceMap;
import com.safecode.andcloud.model.Project;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by zoolsher on 2016/12/26.
 *
 * @author zoolsher
 */
@Repository
public class DeviceMapDao extends BaseDao {
    public void saveOrUpdate(DeviceMap deviceMap) {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(deviceMap);
    }

    public DeviceMap getByProjectAndType(Project project, int type) {
        Query query = this.getSessionFactory().getCurrentSession().createQuery("select dm from DeviceMap dm where dm.project = ?1 and dm.type = ?2");
        query.setParameter("1", project);
        query.setParameter("2", type);
        return (DeviceMap) query.uniqueResult();
    }
}
