package com.dt.user.service.BasePublicService;

import com.dt.user.model.BasePublicModel.BasicPublicProducts;

import java.util.List;

public interface BasicPublicProductsService{


    /**
     * 查询类目信息
     * @return
     */
    List<BasicPublicProducts> findByProductsInfo();
}
