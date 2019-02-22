package com.dt.user.model.BasePublicModel;

/**
 * 汇率表
 */
public class BasicPublicExchangeRate {

  private Long exchangeRateId;
  private Long exchangeRateNumber;
  private Integer currencyId;
  private Double toRmb;
  private Double toUsd;
  private Long effectiveDate;
  private String remark;
  private Boolean status;
  private Long createDate;
  private Long createIdUser;
  private Long modifyDate;
  private Long modifyIdUser;
  private Long auditDate;
  private Long auditIdUser;
  //汇率名称
  private String currencyName;

  public String getCurrencyName() {
    return currencyName;
  }

  public void setCurrencyName(String currencyName) {
    this.currencyName = currencyName;
  }

  public Long getExchangeRateId() {
    return exchangeRateId;
  }

  public void setExchangeRateId(Long exchangeRateId) {
    this.exchangeRateId = exchangeRateId;
  }

  public Long getExchangeRateNumber() {
    return exchangeRateNumber;
  }

  public void setExchangeRateNumber(Long exchangeRateNumber) {
    this.exchangeRateNumber = exchangeRateNumber;
  }

  public Integer getCurrencyId() {
    return currencyId;
  }

  public void setCurrencyId(Integer currencyId) {
    this.currencyId = currencyId;
  }

  public Double getToRmb() {
    return toRmb;
  }

  public void setToRmb(Double toRmb) {
    this.toRmb = toRmb;
  }

  public Double getToUsd() {
    return toUsd;
  }

  public void setToUsd(Double toUsd) {
    this.toUsd = toUsd;
  }

  public Long getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(Long effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
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
