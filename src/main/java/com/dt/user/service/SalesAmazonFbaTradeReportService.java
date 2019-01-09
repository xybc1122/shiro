package com.dt.user.service;

import com.dt.user.model.SalesAmazonFbaTradeReport;

import java.util.List;

public interface SalesAmazonFbaTradeReportService {

    /**
     * 存入订单数据 txt
     *
     * @return
     */
    int AddSalesAmazonAdTrdList(List<SalesAmazonFbaTradeReport> trdList);

}
