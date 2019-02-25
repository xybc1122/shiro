package com.dt.user.service.impl;

import com.dt.user.dto.ExchangeRateDto;
import com.dt.user.mapper.BasePublicMapper.BasicPublicExchangeRateMapper;
import com.dt.user.model.BasePublicModel.BasicPublicExchangeRate;
import com.dt.user.service.BasePublicService.BasicPublicExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicPublicExchangeRateServiceImpl implements BasicPublicExchangeRateService {

    @Autowired
    private BasicPublicExchangeRateMapper rateMapper;

    @Override
    public List<BasicPublicExchangeRate> getRateInfo(ExchangeRateDto rateDto) {
        return rateMapper.getRateInfo(rateDto);
    }
}
