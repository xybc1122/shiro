package com.dt.user.service.SalesAmazonAdService;

import com.dt.user.model.SalesAmazonAd.SalesAmazonAdHl;

import java.util.List;

public interface SalesAmazonAHlService {

    /**
     * 存入广告Hl数据
     *
     * @return
     */
    int AddSalesAmazonAdHlList(List<SalesAmazonAdHl> hlList);
}
