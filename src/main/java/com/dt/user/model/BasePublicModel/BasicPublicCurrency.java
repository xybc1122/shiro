package com.dt.user.model.BasePublicModel;

/**
 * 币别表
 */
public class BasicPublicCurrency {
  /**
   * 币别ID
   */
  private Long currencyId;
  /**
   * 币别编号
   */
  private Long number;
  /**
   * 币别名称
   */
  private String currencyName;
  /**
   * 币别英文简写
   */
  private String currencyShortNameEng;
  /**
   * 币别符号
   */
  private String currencySymbol;
  private Long statusId;

  public Long getCurrencyId() {
    return currencyId;
  }

  public void setCurrencyId(Long currencyId) {
    this.currencyId = currencyId;
  }

  public Long getNumber() {
    return number;
  }

  public void setNumber(Long number) {
    this.number = number;
  }

  public String getCurrencyName() {
    return currencyName;
  }

  public void setCurrencyName(String currencyName) {
    this.currencyName = currencyName;
  }

  public String getCurrencyShortNameEng() {
    return currencyShortNameEng;
  }

  public void setCurrencyShortNameEng(String currencyShortNameEng) {
    this.currencyShortNameEng = currencyShortNameEng;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public Long getStatusId() {
    return statusId;
  }

  public void setStatusId(Long statusId) {
    this.statusId = statusId;
  }
}
