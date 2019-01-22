package com.dt.user.service.SalesAmazonAdService;

import com.dt.user.model.SalesAmazonAd.SalesAmazonAdStr;

import java.util.List;

public interface SalesAmazonAdStrService {
    /**
     * 存入广告数据
     * @param strList
     * @return
     */
    int AddSalesAmazonAdStrList(List<SalesAmazonAdStr> strList);
}
