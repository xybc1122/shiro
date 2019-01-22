package com.dt.user.service.impl;

import com.dt.user.mapper.SalesAmazonAdMapper.SalesAmazonAdStrMapper;
import com.dt.user.model.SalesAmazonAd.SalesAmazonAdStr;
import com.dt.user.service.SalesAmazonAdService.SalesAmazonAdStrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesAmazonAdStrServiceImpl implements SalesAmazonAdStrService {
    @Autowired
    private SalesAmazonAdStrMapper strMapper;

    @Override
    public int AddSalesAmazonAdStrList(List<SalesAmazonAdStr> strList) {
        return strMapper.AddSalesAmazonAdStrList(strList);
    }
}
