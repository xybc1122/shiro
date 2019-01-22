package com.dt.user.service.impl;

import com.dt.user.mapper.SalesAmazonAdMapper.SalesAmazonAdCprMapper;
import com.dt.user.model.SalesAmazonAd.SalesAmazonAdCpr;
import com.dt.user.service.SalesAmazonAdService.SalesAmazonAdCprService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesAmazonAdCprServiceImpl implements SalesAmazonAdCprService {
    @Autowired
    private SalesAmazonAdCprMapper salesAmazonAdCprMapper;

    @Override
    public int AddSalesAmazonAdCprList(List<SalesAmazonAdCpr> cprList) {
        return salesAmazonAdCprMapper.AddSalesAmazonAdCprList(cprList);
    }
}
