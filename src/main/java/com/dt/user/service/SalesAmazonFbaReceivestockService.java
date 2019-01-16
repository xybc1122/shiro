package com.dt.user.service;

import com.dt.user.model.SalesAmazonFbaReceivestock;


import java.util.List;

public interface SalesAmazonFbaReceivestockService {

    /**
     * 存入接收库存数据
     *
     * @return
     */
    int AddSalesAmazonAdReceivestockList(List<SalesAmazonFbaReceivestock> receivesList);
}
