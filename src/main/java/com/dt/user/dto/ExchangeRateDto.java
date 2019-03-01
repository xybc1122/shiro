package com.dt.user.dto;

import com.dt.user.model.BasePublicModel.BasicPublicExchangeRate;
import com.dt.user.model.SystemLogStatus;

public class ExchangeRateDto extends BasicPublicExchangeRate {


    /**
     * 币别名称
     */
    private String currencyName;


    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

}

