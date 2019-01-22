package com.dt.user.service.SalesAmazonAdService;

import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaInventoryEnd;

import java.util.List;

public interface SalesAmazonFbaInventoryEndService {

    /**
     * 存入接收库存数据
     *
     * @return
     */
    int AddSalesAmazonAdInventoryEndList(List<SalesAmazonFbaInventoryEnd> endList);
}
