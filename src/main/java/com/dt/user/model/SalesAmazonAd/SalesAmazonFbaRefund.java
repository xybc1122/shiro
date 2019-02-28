package com.dt.user.model.SalesAmazonAd;


/**
 * 退货表
 */
public class SalesAmazonFbaRefund {

  private Long id;
  private Long date;
  private Long purchaseDate;
  private Integer shopId;
  private Integer siteId;
  private String orderId;
  private String sku;
  private String sAsin;
  private String fnsku;
  private Long skuId;
  private String pName;
  private Integer quantity;
  private String fc;
  private Long awId;
  private String detailedDisposition;
  private String reason;
  private String refundStaus;
  private String licensePlateNumber;
  private String customerRemarks;
  private String remark;
  private Integer status;
  private Long createDate;
  private Long createIdUser;
  private Long modifyDate;
  private Long modifyIdUser;
  private Long auditDate;
  private Long auditIdUser;
  private Long recordingId;

  public SalesAmazonFbaRefund() {

  }

  public SalesAmazonFbaRefund(Integer shopId, Long createDate, Long createIdUser, Long recordingId) {
    this.shopId = shopId;
    this.createDate = createDate;
    this.createIdUser = createIdUser;
    this.recordingId = recordingId;
  }

  public Long getRecordingId() {
    return recordingId;
  }

  public void setRecordingId(Long recordingId) {
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

  public Long getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(Long purchaseDate) {
    this.purchaseDate = purchaseDate;
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

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public String getsAsin() {
    return sAsin;
  }

  public void setsAsin(String sAsin) {
    this.sAsin = sAsin;
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

  public String getpName() {
    return pName;
  }

  public void setpName(String pName) {
    this.pName = pName;
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

  public Long getAwId() {
    return awId;
  }

  public void setAwId(Long awId) {
    this.awId = awId;
  }

  public String getDetailedDisposition() {
    return detailedDisposition;
  }

  public void setDetailedDisposition(String detailedDisposition) {
    this.detailedDisposition = detailedDisposition;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getRefundStaus() {
    return refundStaus;
  }

  public void setRefundStaus(String refundStaus) {
    this.refundStaus = refundStaus;
  }

  public String getLicensePlateNumber() {
    return licensePlateNumber;
  }

  public void setLicensePlateNumber(String licensePlateNumber) {
    this.licensePlateNumber = licensePlateNumber;
  }

  public String getCustomerRemarks() {
    return customerRemarks;
  }

  public void setCustomerRemarks(String customerRemarks) {
    this.customerRemarks = customerRemarks;
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
}
