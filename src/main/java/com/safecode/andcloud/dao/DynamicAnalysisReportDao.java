package com.safecode.andcloud.dao;

import com.safecode.andcloud.model.DynamicAnalysisReport;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by sharp on 2017/1/10.
 */
@Repository
public class DynamicAnalysisReportDao extends BaseDao
{
    public DynamicAnalysisReport findReportByEmulatorId(String emulatorId)
    {
        Query query = this.getSessionFactory().getCurrentSession().createQuery("select rep from DynamicAnalysisReport rep where rep.emulator_id = ?1");
        query.setString("1",emulatorId);

        DynamicAnalysisReport result = (DynamicAnalysisReport) query.uniqueResult();
        return result;
    }

    public void saveOrUpdate(DynamicAnalysisReport report) 
    {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(report);
    }
}
