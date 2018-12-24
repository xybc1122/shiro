package com.dt.user.service;

import com.dt.user.model.SalesAmazonAdCpr;

import java.util.List;

public interface SalesAmazonAdCprService {


    /**
     * 存入广告数据
     * @param cprList
     * @return
     */
    int AddSalesAmazonAdCprList(List<SalesAmazonAdCpr> cprList);
}
