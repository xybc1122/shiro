package com.dt.user.model;


public class SalesAmazonAdOar {

  private Long id;
  private Long date;
  private Long shopId;
  private Long siteId;
  private String campaignName;
  private String adGroupName;
  private String advertisedSku;
  private Long advertisedAsin;
  private String targeting;
  private String matchType;
  private String otherAsin;
  private Long skuId;
  private Double otherAsinUnits;
  private Double otherAsinUnitsOrdered;
  private Double otherAsinUnitsOrderedSales;
  private String remark;
  private Integer status;
  private Long createDate;
  private Long createIdUser;
  private Long modifyDate;
  private Long modifyIdUser;
  private Long auditDate;
  private Long auditIdUser;
  private Long recordingId;

  public SalesAmazonAdOar() {

  }
  public SalesAmazonAdOar(Long shopId, Long siteId, Long createDate, Long createIdUser, Long recordingId) {
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

  public Long getShopId() {
    return shopId;
  }

  public void setShopId(Long shopId) {
    this.shopId = shopId;
  }

  public Long getSiteId() {
    return siteId;
  }

  public void setSiteId(Long siteId) {
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

  public String getAdvertisedSku() {
    return advertisedSku;
  }

  public void setAdvertisedSku(String advertisedSku) {
    this.advertisedSku = advertisedSku;
  }

  public Long getAdvertisedAsin() {
    return advertisedAsin;
  }

  public void setAdvertisedAsin(Long advertisedAsin) {
    this.advertisedAsin = advertisedAsin;
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

  public String getOtherAsin() {
    return otherAsin;
  }

  public void setOtherAsin(String otherAsin) {
    this.otherAsin = otherAsin;
  }

  public Long getSkuId() {
    return skuId;
  }

  public void setSkuId(Long skuId) {
    this.skuId = skuId;
  }

  public Double getOtherAsinUnits() {
    return otherAsinUnits;
  }

  public void setOtherAsinUnits(Double otherAsinUnits) {
    this.otherAsinUnits = otherAsinUnits;
  }

  public Double getOtherAsinUnitsOrdered() {
    return otherAsinUnitsOrdered;
  }

  public void setOtherAsinUnitsOrdered(Double otherAsinUnitsOrdered) {
    this.otherAsinUnitsOrdered = otherAsinUnitsOrdered;
  }

  public Double getOtherAsinUnitsOrderedSales() {
    return otherAsinUnitsOrderedSales;
  }

  public void setOtherAsinUnitsOrderedSales(Double otherAsinUnitsOrderedSales) {
    this.otherAsinUnitsOrderedSales = otherAsinUnitsOrderedSales;
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
