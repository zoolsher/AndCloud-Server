package com.safecode.andcloud.dao;

import com.safecode.andcloud.model.SimulatorDomain;
import org.springframework.stereotype.Repository;

/**
 * 虚拟机镜像
 *
 * @author sumy
 */
@Repository
public class SimulatorDomainDao extends BaseDao {

    public void saveOrUpdate(SimulatorDomain simulatorDomain) {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(simulatorDomain);
    }

}
