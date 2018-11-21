package com.dt.user.service;

import com.dt.user.model.BasicPublicShop;

import java.util.List;

public interface BasicPublicShopService {


    /**
     * 查询店铺所有相关信息
     * @return
     */
    List<BasicPublicShop> findByListShop();
}
