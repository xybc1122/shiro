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
    public Long selSkuId(Integer sId, Integer siteId, String skuName) {
        if (StringUtils.isEmpty(skuName)) {
            return 0L;
        }
        Long skuId = basicSalesAmazonSkuMapper.getSkuId(sId, siteId, skuName);
        if (skuId != null) {
            return skuId;
        }
        return null;
    }

    @Override
    public Long getAsinSkuId(Integer sId, Integer siteId, String sAsin) {
        if (StringUtils.isEmpty(sAsin)) {
            return 0L;
        }
        Long skuId = basicSalesAmazonSkuMapper.getAsinSkuId(sId, siteId, sAsin);
        if (skuId != null) {
            return skuId;
        }
        return null;
    }
}
