package com.safecode.andcloud.dao;

import com.safecode.andcloud.model.ReportItem;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sharp on 2017/1/10.
 */
@Repository
public class ReportDao extends BaseDao {
    public List<ReportItem> findReportItemsByEmulatorId(int emulatorId) {
        Query query = this.getSessionFactory().getCurrentSession().createQuery("select rep from ReportItem rep where rep.simulatorDomain.id = ?1");
        query.setInteger("1", emulatorId);

        List<ReportItem> result = (List<ReportItem>) query.list();
        return result;
    }

    public void saveReportItemList(List<ReportItem> reportItems) {
        for (ReportItem item : reportItems) {
            this.saveOrUpdate(item);
        }
    }

    public void saveOrUpdate(ReportItem report) {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(report);
    }
}
