package com.dt.user.dto;

import com.dt.user.model.BasePublicModel.BasicPublicSite;

public class SiteDto extends BasicPublicSite {

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

    /**
     * 币别英文简写
     */
    private String currencyEngShort;

    /**
     * 区域名称
     */
    private String areaName;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCurrencyEngShort() {
        return currencyEngShort;
    }

    public void setCurrencyEngShort(String currencyEngShort) {
        this.currencyEngShort = currencyEngShort;
    }

    public String getCurrencyName() {
        return currencyName;
    }

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
