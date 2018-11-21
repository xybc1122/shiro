package com.dt.user.model;

/**
 * 站点类
 */
public class BasicPublicSite {
    /**
     * 站点ID
     */
    private Long siteId;
    /**
     * 站点编号
     */
    private Long siteNumber;
    /**
     * 站点名称
     */
    private String siteName;
    /**
     * 英文简称
     */
    private String siteEng;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 币别名称
     */
    private String currencyName;
    /**
     * 币别英文简写
     */
    private String currencyEngShort;
    /**
     * URL
     */
    private String url;
    /**
     * VAT税率
     */
    private String vat;
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
    private Long createIdUser;
    /**
     * 修改日期
     */
    private Long modifyDate;
    /**
     * 修改人
     */
    private Long modifyIdUser;
    /**
     * 审核时间
     */
    private Long auditDate;
    /**
     * 审核人
     */
    private Long auditIdUser;

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyEngShort() {
        return currencyEngShort;
    }

    public void setCurrencyEngShort(String currencyEngShort) {
        this.currencyEngShort = currencyEngShort;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getSiteNumber() {
        return siteNumber;
    }

    public void setSiteNumber(Long siteNumber) {
        this.siteNumber = siteNumber;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteEng() {
        return siteEng;
    }

    public void setSiteEng(String siteEng) {
        this.siteEng = siteEng;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
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

    public Long getCreateIdUser() {
        return createIdUser;
    }

    public void setCreateIdUser(Long createIdUser) {
        this.createIdUser = createIdUser;
    }

    public Long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getModifyIdUser() {
        return modifyIdUser;
    }

    public void setModifyIdUser(Long modifyIdUser) {
        this.modifyIdUser = modifyIdUser;
    }

    public Long getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Long auditDate) {
        this.auditDate = auditDate;
    }

    public Long getAuditIdUser() {
        return auditIdUser;
    }

    public void setAuditIdUser(Long auditIdUser) {
        this.auditIdUser = auditIdUser;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
