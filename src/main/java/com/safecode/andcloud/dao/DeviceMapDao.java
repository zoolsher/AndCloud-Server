package com.safecode.andcloud.dao;

import com.safecode.andcloud.model.DeviceMap;
import org.springframework.stereotype.Repository;

/**
 * Created by zoolsher on 2016/12/26.
 * @author zoolsher
 */
@Repository
public class DeviceMapDao extends BaseDao {
    public void saveOrUpdate(DeviceMap deviceMap) {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(deviceMap);
    }
}
