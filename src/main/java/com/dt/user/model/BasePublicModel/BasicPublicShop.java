package com.dt.user.model.BasePublicModel;

/**
 * 店铺表
 */
public class BasicPublicShop {
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 店铺编号
     */
    private Long shopNumber;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 店铺英文
     */
    private String shopEng;
    /**
     * 公司名称
     */
    private String companyFullName;
    /**
     *平台名称
     */
    private String platformTypeName;
    /**
     * 负责人
     */
    private String principal;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Long createDate;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 修改日期
     */
    private Long modifyDate;
    /**
     * 修改人
     */
    private String modifyUser;
    /**
     * 审核时间
     */
    private Long auditDate;
    /**
     * 审核人
     */
    private String auditUser;

    public String getPlatformTypeName() {
        return platformTypeName;
    }

    public void setPlatformTypeName(String platformTypeName) {
        this.platformTypeName = platformTypeName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(Long shopNumber) {
        this.shopNumber = shopNumber;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopEng() {
        return shopEng;
    }

    public void setShopEng(String shopEng) {
        this.shopEng = shopEng;
    }

    public String getCompanyFullName() {
        return companyFullName;
    }

    public void setCompanyFullName(String companyFullName) {
        this.companyFullName = companyFullName;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Long auditDate) {
        this.auditDate = auditDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }
}
