package com.dt.user.model.SalesAmazonAd;


public class SalesAmazonAdStr {

    private Long id;
    private Long date;
    private Integer shopId;
    private Integer siteId;
    private String campaignName;
    private String adGroupName;
    private String targeting;
    private String matchType;
    private String customerSearchTerm;
    private Double impressions;
    private Double clicks;
    private Double totalSpend;
    private Double sales;
    private Double roas;
    private Double ordersPlaced;
    private Double totalUnits;
    private Double advertisedSkuUnitsOrdered;
    private Double otherSkuUnitsOrdered;
    private Double advertisedSkuUnitsSales;
    private Double otherSkuUnitsSales;
    private String remark;
    private Long status;
    private Long createDate;
    private Long createIdUser;
    private Long modifyDate;
    private Long modifyIdUser;
    private Long auditDate;
    private Long auditIdUser;
    private Long recordingId;

    public SalesAmazonAdStr() {

    }

    public SalesAmazonAdStr(Integer shopId, Integer siteId, Long createDate, Long createIdUser, Long recordingId) {
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

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getAdGroupName() {
        return adGroupName;
    }

    public void setAdGroupName(String adGroupName) {
        this.adGroupName = adGroupName;
    }

    public String getTargeting() {
        return targeting;
    }

    public void setTargeting(String targeting) {
        this.targeting = targeting;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getCustomerSearchTerm() {
        return customerSearchTerm;
    }

    public void setCustomerSearchTerm(String customerSearchTerm) {
        this.customerSearchTerm = customerSearchTerm;
    }

    public Double getImpressions() {
        return impressions;
    }

    public void setImpressions(Double impressions) {
        this.impressions = impressions;
    }

    public Double getClicks() {
        return clicks;
    }

    public void setClicks(Double clicks) {
        this.clicks = clicks;
    }

    public Double getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(Double totalSpend) {
        this.totalSpend = totalSpend;
    }

    public Double getSales() {
        return sales;
    }

    public void setSales(Double sales) {
        this.sales = sales;
    }

    public Double getRoas() {
        return roas;
    }

    public void setRoas(Double roas) {
        this.roas = roas;
    }

    public Double getOrdersPlaced() {
        return ordersPlaced;
    }

    public void setOrdersPlaced(Double ordersPlaced) {
        this.ordersPlaced = ordersPlaced;
    }

    public Double getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(Double totalUnits) {
        this.totalUnits = totalUnits;
    }

    public Double getAdvertisedSkuUnitsOrdered() {
        return advertisedSkuUnitsOrdered;
    }

    public void setAdvertisedSkuUnitsOrdered(Double advertisedSkuUnitsOrdered) {
        this.advertisedSkuUnitsOrdered = advertisedSkuUnitsOrdered;
    }

    public Double getOtherSkuUnitsOrdered() {
        return otherSkuUnitsOrdered;
    }

    public void setOtherSkuUnitsOrdered(Double otherSkuUnitsOrdered) {
        this.otherSkuUnitsOrdered = otherSkuUnitsOrdered;
    }

    public Double getAdvertisedSkuUnitsSales() {
        return advertisedSkuUnitsSales;
    }

    public void setAdvertisedSkuUnitsSales(Double advertisedSkuUnitsSales) {
        this.advertisedSkuUnitsSales = advertisedSkuUnitsSales;
    }

    public Double getOtherSkuUnitsSales() {
        return otherSkuUnitsSales;
    }

    public void setOtherSkuUnitsSales(Double otherSkuUnitsSales) {
        this.otherSkuUnitsSales = otherSkuUnitsSales;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
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
