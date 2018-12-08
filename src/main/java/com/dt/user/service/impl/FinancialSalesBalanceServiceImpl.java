package com.dt.user.service.impl;

import com.dt.user.mapper.FinancialSalesBalanceMapper;
import com.dt.user.model.FinancialSalesBalance;
import com.dt.user.service.FinancialSalesBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinancialSalesBalanceServiceImpl implements FinancialSalesBalanceService {

    @Autowired
    private FinancialSalesBalanceMapper financialSalesBalanceMapper;

    @Override
    public int addInfoGerman(FinancialSalesBalance financialSalesBalance) {
        return financialSalesBalanceMapper.addInfoGerman(financialSalesBalance);
    }
}
