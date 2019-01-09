package com.dt.user.service;

import com.dt.user.model.FinancialSalesBalance;

import java.util.List;

public interface FinancialSalesBalanceService {

    /**
     * 财务存入信息
     * @param fsbList
     * @param tbId
     * @return
     */
    int addInfo(List<FinancialSalesBalance> fsbList,Integer tbId);
}
