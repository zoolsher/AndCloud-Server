package com.safecode.andcloud.service;

import com.safecode.andcloud.dao.ReportDao;
import com.safecode.andcloud.model.ReportItem;
import com.safecode.andcloud.model.SimulatorDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sharp on 2017/1/10.
 */
@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportDao reportDao;

    public void saveReportItem(ReportItem report) {
        reportDao.saveOrUpdate(report);
    }


    public void saveReportItemList(List<ReportItem> reportItems) {
        reportDao.saveReportItemList(reportItems);
    }

    public void saveReportItemListToEmulator(List<ReportItem> reportItems, SimulatorDomain simulatorDomain) {
        for (ReportItem reportItem : reportItems) {
            reportItem.setSimulatorDomain(simulatorDomain);
            reportDao.saveOrUpdate(reportItem);
        }
    }
}
