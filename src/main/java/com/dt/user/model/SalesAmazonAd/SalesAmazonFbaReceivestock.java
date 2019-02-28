package com.dt.user.model.SalesAmazonAd;

/**
 * 接收库存
 */
public class SalesAmazonFbaReceivestock {

  private Long id;
  private Long date;
  private Integer shopId;
  private Integer siteId;
  private String fbaShipmentId;
  private String sku;
  private String fnsku;
  private Long skuId;
  private String productName;
  private Integer quantity;
  private String fc;
  private Integer awId;
  private String remark;
  private Integer status;
  private Long createDate;
  private Long createIdUser;
  private Long modifyDate;
  private Long modifyIdUser;
  private Long auditDate;
  private Long auditIdUser;
  private Long recordingId;

  public SalesAmazonFbaReceivestock() {

  }

  public SalesAmazonFbaReceivestock(Integer shopId, Long createDate, Long createIdUser, Long recordingId) {
    this.shopId = shopId;
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

  public String getFbaShipmentId() {
    return fbaShipmentId;
  }

  public void setFbaShipmentId(String fbaShipmentId) {
    this.fbaShipmentId = fbaShipmentId;
  }

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public String getFnsku() {
    return fnsku;
  }

  public void setFnsku(String fnsku) {
    this.fnsku = fnsku;
  }

  public Long getSkuId() {
    return skuId;
  }

  public void setSkuId(Long skuId) {
    this.skuId = skuId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public String getFc() {
    return fc;
  }

  public void setFc(String fc) {
    this.fc = fc;
  }

  public Integer getAwId() {
    return awId;
  }

  public void setAwId(Integer awId) {
    this.awId = awId;
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
