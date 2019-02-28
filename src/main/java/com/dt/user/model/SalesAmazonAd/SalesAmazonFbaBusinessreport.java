package com.dt.user.model.SalesAmazonAd;

/**
 * 业务报告导入数据
 */
public class SalesAmazonFbaBusinessreport {

    private Long id;
    private Long date;
    private Integer shopId;
    private Integer siteId;
    private String sku;
    private Long skuId;
    private String fAsin;
    private String sAsin;
    private String pName;
    private Integer sessionsVisit;
    private Double sessionsPer;
    private Integer pageViews;
    private Double buyBoxPer;
    private Integer order;
    private Integer orderB2B;
    private Double sales;
    private Double salesB2B;
    private Integer orderItems;
    private Integer orderItemsB2B;
    private String remark;
    private Integer status;
    private Long createDate;
    private Long createIdUser;
    private Long modifyDate;
    private Long modifyIdUser;
    private Long auditDate;
    private Long auditIdUser;
    private Long recordingId;

    public SalesAmazonFbaBusinessreport() {

    }

    public SalesAmazonFbaBusinessreport(Integer shopId, Integer siteId, Long createDate, Long createIdUser, Long recordingId) {
        this.shopId = shopId;
        this.siteId = siteId;
        this.createDate = createDate;
        this.createIdUser = createIdUser;
        this.recordingId = recordingId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getfAsin() {
        return fAsin;
    }

    public void setfAsin(String fAsin) {
        this.fAsin = fAsin;
    }

    public String getsAsin() {
        return sAsin;
    }

    public void setsAsin(String sAsin) {
        this.sAsin = sAsin;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Integer getSessionsVisit() {
        return sessionsVisit;
    }

    public void setSessionsVisit(Integer sessionsVisit) {
        this.sessionsVisit = sessionsVisit;
    }

    public Double getSessionsPer() {
        return sessionsPer;
    }

    public void setSessionsPer(Double sessionsPer) {
        this.sessionsPer = sessionsPer;
    }

    public Integer getPageViews() {
        return pageViews;
    }

    public void setPageViews(Integer pageViews) {
        this.pageViews = pageViews;
    }

    public Double getBuyBoxPer() {
        return buyBoxPer;
    }

    public void setBuyBoxPer(Double buyBoxPer) {
        this.buyBoxPer = buyBoxPer;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getOrderB2B() {
        return orderB2B;
    }

    public void setOrderB2B(Integer orderB2B) {
        this.orderB2B = orderB2B;
    }

    public Double getSales() {
        return sales;
    }

    public void setSales(Double sales) {
        this.sales = sales;
    }

    public Double getSalesB2B() {
        return salesB2B;
    }

    public void setSalesB2B(Double salesB2B) {
        this.salesB2B = salesB2B;
    }

    public Integer getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Integer orderItems) {
        this.orderItems = orderItems;
    }

    public Integer getOrderItemsB2B() {
        return orderItemsB2B;
    }

    public void setOrderItemsB2B(Integer orderItemsB2B) {
        this.orderItemsB2B = orderItemsB2B;
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

    public Long getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(Long recordingId) {
        this.recordingId = recordingId;
    }
}
