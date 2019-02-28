package com.dt.user.service.SalesAmazonAdService;

import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaTradeReport;

import java.util.List;

public interface SalesAmazonFbaTradeReportService {

    /**
     * 存入订单数据 txt
     *
     * @return
     */
    int AddSalesAmazonAdTrdList(List<SalesAmazonFbaTradeReport> trdList);

    /**
     * 通过站点店铺ID 跟订单号 查询 下单时间 站点 ID
     */
    SalesAmazonFbaTradeReport getReport(Integer sId,String oId);
}
