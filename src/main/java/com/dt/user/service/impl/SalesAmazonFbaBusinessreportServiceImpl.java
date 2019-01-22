package com.dt.user.service.impl;

import com.dt.user.mapper.SalesAmazonAdMapper.SalesAmazonFbaBusinessreportMapper;
import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaBusinessreport;
import com.dt.user.service.SalesAmazonAdService.SalesAmazonFbaBusinessreportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesAmazonFbaBusinessreportServiceImpl implements SalesAmazonFbaBusinessreportService {
    @Autowired
    private SalesAmazonFbaBusinessreportMapper busMapper;

    @Override
    public int AddSalesAmazonAdBusList(List<SalesAmazonFbaBusinessreport> hlList) {
        return busMapper.AddSalesAmazonAdHlList(hlList);
    }
}
