package com.dt.user.dto;

import com.dt.user.model.BasePublicModel.BasicPublicExchangeRate;

public class ExchangeRateDto extends BasicPublicExchangeRate {


    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 显示的页数
     */
    private Integer pageSize;
    /**
     * 币别名称
     */
    private String currencyName;

    @Override
    public String getCurrencyName() {
        return currencyName;
    }

    @Override
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
