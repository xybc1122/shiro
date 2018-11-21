package com.dt.user.model;

/**
 * 币别类
 */
public class BasicPublicCurrency {
  /**
   * 币别ID
   */
  private Long currencyId;
  /**
   * 币别编号
   */
  private Long currencyNumber;
  /**
   * 币别名称
   */
  private String currencyName;
  /**
   * 币别英文简写
   */
  private String currencyEngShort;
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

  public Long getCurrencyId() {
    return currencyId;
  }

  public void setCurrencyId(Long currencyId) {
    this.currencyId = currencyId;
  }

  public Long getCurrencyNumber() {
    return currencyNumber;
  }

  public void setCurrencyNumber(Long currencyNumber) {
    this.currencyNumber = currencyNumber;
  }

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
