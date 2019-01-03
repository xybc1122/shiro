package com.dt.user.service;

import com.dt.user.model.SalesAmazonAdOar;

import java.util.List;

public interface SalesAmazonAdOarService {

    /**
     * 存入广告Oar数据
     *
     * @return
     */
    int AddSalesAmazonAdOarList(List<SalesAmazonAdOar> oarList);
}
