package com.dt.user.service.impl;

import com.dt.user.mapper.SalesAmazonAdMapper.SalesAmazonFbaInventoryEndMapper;
import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaInventoryEnd;
import com.dt.user.service.SalesAmazonAdService.SalesAmazonFbaInventoryEndService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesAmazonFbaInventoryEndServiceImpl implements SalesAmazonFbaInventoryEndService {
    @Autowired
    private SalesAmazonFbaInventoryEndMapper endMapper;
    @Override
    public int AddSalesAmazonAdInventoryEndList(List<SalesAmazonFbaInventoryEnd> endList) {
        return endMapper.AddSalesAmazonAdInventoryEndList(endList);
    }
}
