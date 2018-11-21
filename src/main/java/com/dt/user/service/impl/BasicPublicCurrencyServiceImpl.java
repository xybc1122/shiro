package com.dt.user.service.impl;

import com.dt.user.mapper.BasicPublicCurrencyMapper;
import com.dt.user.model.BasicPublicCurrency;
import com.dt.user.service.BasicPublicCurrencyService;
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
