package com.dt.user.service.impl;

import com.dt.user.mapper.SalesAmazonFbaReceivestockMapper;
import com.dt.user.model.SalesAmazonFbaReceivestock;
import com.dt.user.service.SalesAmazonFbaReceivestockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesAmazonFbaReceivestockServiceImpl implements SalesAmazonFbaReceivestockService {
    @Autowired
    private SalesAmazonFbaReceivestockMapper sfrMapper;


    @Override
    public int AddSalesAmazonAdReceivestockList(List<SalesAmazonFbaReceivestock> receivesList) {
        return sfrMapper.AddSalesAmazonAdReceivestockList(receivesList);
    }
}
