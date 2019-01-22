package com.dt.user.service.impl;

import com.dt.user.mapper.SalesAmazonAdMapper.SalesAmazonAHlMapper;
import com.dt.user.model.SalesAmazonAd.SalesAmazonAdHl;
import com.dt.user.service.SalesAmazonAdService.SalesAmazonAHlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesAmazonAHlServiceImpl implements SalesAmazonAHlService {
    @Autowired
    private SalesAmazonAHlMapper aHlMapper;

    @Override
    public int AddSalesAmazonAdHlList(List<SalesAmazonAdHl> hlList) {
        return aHlMapper.AddSalesAmazonAdHlList(hlList);
    }
}
