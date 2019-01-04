package com.dt.user.service;

import com.dt.user.model.SalesAmazonAdHl;

import java.util.List;

public interface SalesAmazonAHlService {

    /**
     * 存入广告Hl数据
     *
     * @return
     */
    int AddSalesAmazonAdHlList(List<SalesAmazonAdHl> hlList);
}
