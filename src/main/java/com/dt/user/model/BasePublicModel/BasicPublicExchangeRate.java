package com.dt.user.model.BasePublicModel;

/**
 * 汇率表
 */
public class BasicPublicExchangeRate {

  private Long exchangeRateId;
  private Integer currencyId;
  private Double toRmb;
  private Double toUsd;
  private Long effectiveDate;
  private Long statusId;

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

  public Long getStatusId() {
    return statusId;
  }

  public void setStatusId(Long statusId) {
    this.statusId = statusId;
  }
}
