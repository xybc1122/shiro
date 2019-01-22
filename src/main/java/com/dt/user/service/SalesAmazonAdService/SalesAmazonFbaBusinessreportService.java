package com.dt.user.service.SalesAmazonAdService;

import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaBusinessreport;

import java.util.List;

public interface SalesAmazonFbaBusinessreportService {

    /**
     * 存业务数据
     *
     * @return
     */
    int AddSalesAmazonAdBusList(List<SalesAmazonFbaBusinessreport> hlList);

}
