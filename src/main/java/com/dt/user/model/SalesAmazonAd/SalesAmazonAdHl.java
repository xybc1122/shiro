package com.dt.user.model.SalesAmazonAd;

/**
 * HL数据导入
 */
public class SalesAmazonAdHl {

  private Long id;
  private Long date;
  private Integer shopId;
  private Integer siteId;
  private String campaignName;
  private Double impressions;
  private Double clicks;
  private Double ctr;
  private Double cpc;
  private Double spend;
  private Double acos;
  private Double roas;
  private Double totalSales;
  private Double totalOrders;
  private Double totalUnits;
  private Double conversionRate;
  private String remark;
  private Integer status;
  private Long createDate;
  private Long createIdUser;
  private Long modifyDate;
  private Long modifyIdUser;
  private Long auditDate;
  private Long auditIdUser;
  private Long recordingId;

  public SalesAmazonAdHl() {

  }

  public SalesAmazonAdHl(Integer shopId, Integer siteId, Long createDate, Long createIdUser, Long recordingId) {
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

  public Double getCtr() {
    return ctr;
  }

  public void setCtr(Double ctr) {
    this.ctr = ctr;
  }

  public Double getCpc() {
    return cpc;
  }

  public void setCpc(Double cpc) {
    this.cpc = cpc;
  }

  public Double getSpend() {
    return spend;
  }

  public void setSpend(Double spend) {
    this.spend = spend;
  }

  public Double getAcos() {
    return acos;
  }

  public void setAcos(Double acos) {
    this.acos = acos;
  }

  public Double getRoas() {
    return roas;
  }

  public void setRoas(Double roas) {
    this.roas = roas;
  }

  public Double getTotalSales() {
    return totalSales;
  }

  public void setTotalSales(Double totalSales) {
    this.totalSales = totalSales;
  }

  public Double getTotalOrders() {
    return totalOrders;
  }

  public void setTotalOrders(Double totalOrders) {
    this.totalOrders = totalOrders;
  }

  public Double getTotalUnits() {
    return totalUnits;
  }

  public void setTotalUnits(Double totalUnits) {
    this.totalUnits = totalUnits;
  }

  public Double getConversionRate() {
    return conversionRate;
  }

  public void setConversionRate(Double conversionRate) {
    this.conversionRate = conversionRate;
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
