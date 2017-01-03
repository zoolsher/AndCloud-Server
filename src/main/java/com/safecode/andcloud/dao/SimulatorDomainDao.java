package com.safecode.andcloud.dao;

import com.safecode.andcloud.model.SimulatorDomain;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @SuppressWarnings("unchecked")
    public List<SimulatorDomain> findByIsDelete(boolean isDelete)
    {
        Query query = this.getSessionFactory().getCurrentSession().createQuery("select en from SimulatorDomain en where en.isdelete = ?1");
        query.setBoolean("1",isDelete);
        return query.list();
    }


}
