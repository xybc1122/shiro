package com.dt.user.service.BasePublicService;
public interface BasicSalesAmazonSkuService {


    /**
     * 通过店铺ID  站点ID  skuName 查找 skuId
     * @param sId
     * @param siteId
     * @param skuName
     * @return
     */
    Long selSkuId(Long sId, Long siteId, String skuName);
}
