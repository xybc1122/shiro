package com.dt.user.service.impl;

import com.dt.user.mapper.BasePublicMapper.BasicSalesAmazonCsvTxtXslHeaderMapper;
import com.dt.user.service.BasePublicService.BasicSalesAmazonCsvTxtXslHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicSalesAmazonCsvTxtXslHeaderServiceImpl implements BasicSalesAmazonCsvTxtXslHeaderService {
    @Autowired
    private BasicSalesAmazonCsvTxtXslHeaderMapper basicSalesAmazonCsvTxtXslHeaderMapper;

    @Override
    public List<String> headerList(Long seId, Integer tbId) {
        return basicSalesAmazonCsvTxtXslHeaderMapper.headerList(seId, tbId);
    }
}
