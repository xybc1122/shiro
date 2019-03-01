package com.dt.user.dto;

import com.dt.user.model.BasePublicModel.BasicPublicCountry;
import com.dt.user.model.SystemLogStatus;

public class CountryDto extends BasicPublicCountry {
    /**
     * 洲名字
     */
    private String provinceName;
    /**
     * 市名字
     */
    private String cityName;
    /**
     * 县区名字
     */
    private String countyName;

    /**
     * 语言名称
     */
    private String languageName;

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
}
