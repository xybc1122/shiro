package com.dt.user.model.BasePublicModel;

import com.dt.user.model.ParentSysTemLog;

/**
 * 仓库表
 */
public class BasicSalesAmazonWarehouse extends ParentSysTemLog {

    private Integer amazonWarehouseId;
    private Long amazonWarehouseNumber;
    private Integer siteId;
    private String warehouseCode;
    private String country;
    private String location;
    private String city;
    private String state;
    private String zip;

    public Integer getAmazonWarehouseId() {
        return amazonWarehouseId;
    }

    public void setAmazonWarehouseId(Integer amazonWarehouseId) {
        this.amazonWarehouseId = amazonWarehouseId;
    }

    public Long getAmazonWarehouseNumber() {
        return amazonWarehouseNumber;
    }

    public void setAmazonWarehouseNumber(Long amazonWarehouseNumber) {
        this.amazonWarehouseNumber = amazonWarehouseNumber;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
