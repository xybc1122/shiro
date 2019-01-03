package com.dt.user.service.impl;

import com.dt.user.mapper.BasePublicMapper.BasicSalesAmazonSkuMapper;
import com.dt.user.model.BasePublicModel.BasicSalesAmazonSku;
import com.dt.user.service.BasePublicService.BasicSalesAmazonSkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicSalesAmazonSkuServiceImpl implements BasicSalesAmazonSkuService {
    @Autowired
    private BasicSalesAmazonSkuMapper basicSalesAmazonSkuMapper;

    @Override
    public Long selSkuId(Long sId, Long siteId, String skuName) {
        if (StringUtils.isEmpty(skuName)) {
            return 0L;
        }
        BasicSalesAmazonSku basicSalesAmazonSku = basicSalesAmazonSkuMapper.getSkuId(sId, siteId, skuName);
        if (basicSalesAmazonSku != null) {
            return basicSalesAmazonSku.getSkuId();
        }
        return null;
    }

    @Override
    public Long getAsinSkuId(Long sId, Long siteId, String sAsin) {
        if (StringUtils.isEmpty(sAsin)) {
            return 0L;
        }
        BasicSalesAmazonSku basicSalesAmazonSku = basicSalesAmazonSkuMapper.getAsinSkuId(sId, siteId, sAsin);
        if (basicSalesAmazonSku != null) {
            return basicSalesAmazonSku.getSkuId();
        }
        return null;
    }
}
