package com.dt.user.model.BasePublicModel;

/**
 * 运输性质
 */
public class BasicLogisticsmgtTransportProperty {

  private Long transportPropertyId;
  private Long transportPropertyNumber;
  private String transportPropertyName;
  private String principal;
  private String remark;
  private Integer status;
  private Long createDate;
  private Long createIdUser;
  private Long modifyDate;
  private Long modifyIdUser;
  private Long auditDate;
  private Long auditIdUser;

  public Long getTransportPropertyId() {
    return transportPropertyId;
  }

  public void setTransportPropertyId(Long transportPropertyId) {
    this.transportPropertyId = transportPropertyId;
  }

  public Long getTransportPropertyNumber() {
    return transportPropertyNumber;
  }

  public void setTransportPropertyNumber(Long transportPropertyNumber) {
    this.transportPropertyNumber = transportPropertyNumber;
  }

  public String getTransportPropertyName() {
    return transportPropertyName;
  }

  public void setTransportPropertyName(String transportPropertyName) {
    this.transportPropertyName = transportPropertyName;
  }

  public String getPrincipal() {
    return principal;
  }

  public void setPrincipal(String principal) {
    this.principal = principal;
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
