package com.safecode.andcloud.dao;

import com.safecode.andcloud.model.CheckedAPI;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sharp on 2017/1/10.
 */
@Repository
public class CheckedAPIdao extends BaseDao
{
    public List<CheckedAPI> findAPIsByEmulatorId(String emulatorID)
    {
        Query query = this.getSessionFactory().getCurrentSession().createQuery("select api from CheckedAPI api where api.emulatorID = ?1");
        query.setString("1",emulatorID);

        List<CheckedAPI> result = query.list();
        return result;
    }

    public void saveOrUpdate(CheckedAPI api)
    {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(api);
    }

    public void saveOrUpdate(List<CheckedAPI> apis)
    {
        for(CheckedAPI api : apis)
        {
            saveOrUpdate(api);
        }
    }
}
