package com.dt.user.service.impl;

import com.dt.user.mapper.BasePublicMapper.BasicPublicCurrencyMapper;
import com.dt.user.model.BasePublicModel.BasicPublicCurrency;
import com.dt.user.service.BasePublicService.BasicPublicCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BasicPublicCurrencyServiceImpl implements BasicPublicCurrencyService {
    @Autowired
    private BasicPublicCurrencyMapper basicPublicCurrencyMapper;

    @Override
    public List<BasicPublicCurrency> findByListCurrency() {
        return basicPublicCurrencyMapper.findByListCurrency();
    }
}
