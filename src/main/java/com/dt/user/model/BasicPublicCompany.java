package com.dt.user.model;

/**
 * 公司表
 */
public class BasicPublicCompany {
  /**
   * 公司ID
   */
  private Long companyId;
  /**
   * 公司编号
   */
  private Long companyNumber;
  /**
   * 公司全称
   */
  private String companyFullName;
  /**
   * 公司简称
   */
  private String companyShortName;
  /**
   * 信用代码
   */
  private String creditCode;
  /**
   * 开户行
   */
  private String bankOfDeposit;
  /**
   * 银行账号
   */
  private String bankAccount;
  /**
   * 账号类型
   */
  private String accountType;
  /**
   * 公司地址
   */
  private String address;
  /**
   * 公司电话
   */
  private String telPhone;
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

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }

  public Long getCompanyNumber() {
    return companyNumber;
  }

  public void setCompanyNumber(Long companyNumber) {
    this.companyNumber = companyNumber;
  }

  public String getCompanyFullName() {
    return companyFullName;
  }

  public void setCompanyFullName(String companyFullName) {
    this.companyFullName = companyFullName;
  }

  public String getCompanyShortName() {
    return companyShortName;
  }

  public void setCompanyShortName(String companyShortName) {
    this.companyShortName = companyShortName;
  }

  public String getCreditCode() {
    return creditCode;
  }

  public void setCreditCode(String creditCode) {
    this.creditCode = creditCode;
  }

  public String getBankOfDeposit() {
    return bankOfDeposit;
  }

  public void setBankOfDeposit(String bankOfDeposit) {
    this.bankOfDeposit = bankOfDeposit;
  }

  public String getBankAccount() {
    return bankAccount;
  }

  public void setBankAccount(String bankAccount) {
    this.bankAccount = bankAccount;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getTelPhone() {
    return telPhone;
  }

  public void setTelPhone(String telPhone) {
    this.telPhone = telPhone;
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
