package com.dt.user.model.BasePublicModel;

import com.dt.user.model.ParentSysTemLog;

/**
 * 店铺表
 */
public class BasicPublicShop extends ParentSysTemLog {
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 店铺编号
     */
    private Long number;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 店铺英文
     */
    private String shopNameEng;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     *平台名称
     */
    private String platformTypeName;
    /**
     * 负责人
     */
    private String principal;
    /**
     * 短代码
     */
    private String shopShortCode;


    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopNameEng() {
        return shopNameEng;
    }

    public void setShopNameEng(String shopNameEng) {
        this.shopNameEng = shopNameEng;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPlatformTypeName() {
        return platformTypeName;
    }

    public void setPlatformTypeName(String platformTypeName) {
        this.platformTypeName = platformTypeName;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getShopShortCode() {
        return shopShortCode;
    }

    public void setShopShortCode(String shopShortCode) {
        this.shopShortCode = shopShortCode;
    }

}
