package com.dt.user.service.BasePublicService;

import com.dt.user.model.BasePublicModel.BasicSalesAmazonSku;

public interface BasicSalesAmazonSkuService {


    /**
     * 通过店铺ID  站点ID  skuName 查找 skuId
     * @param sId
     * @param siteId
     * @param skuName
     * @return
     */
    Long selSkuId(Long sId, Long siteId, String skuName);


    /**
     * 通过店铺ID  站点ID  sAsin 查找 skuId
     * @param sId
     * @param siteId
     * @return
     */
    Long getAsinSkuId(Long sId, Long siteId,String sAsin);
}
