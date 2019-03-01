package com.dt.user.dto;

import com.dt.user.model.BasePublicModel.BasicPublicSite;
import com.dt.user.model.SystemLogStatus;

public class SiteDto extends BasicPublicSite {

    /**
     * 币别名称
     */
    private String currencyName;

    /**
     * 币别英文简写
     */
    private String currencyShortNameEng;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 站点负责人名称
     */
    private String employeeName;

    /**
     * 国家名称
     */
    private String countryName;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCurrencyShortNameEng() {
        return currencyShortNameEng;
    }

    public void setCurrencyShortNameEng(String currencyShortNameEng) {
        this.currencyShortNameEng = currencyShortNameEng;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

}
