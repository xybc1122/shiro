package com.dt.user.service.SalesAmazonAdService;

import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaReceivestock;


import java.util.List;

public interface SalesAmazonFbaReceivestockService {

    /**
     * 存入接收库存数据
     *
     * @return
     */
    int AddSalesAmazonAdReceivestockList(List<SalesAmazonFbaReceivestock> receivesList);
}
