package com.dt.user.model;

/**
 * 区域类
 */
public class BasicPublicArea {
  /**
   * 区域ID
   */
  private Long areaId;
  /**
   * 区域编号
   */
  private Long areaNumber;
  /**
   * 区域名称
   */
  private String areaName;
  /**
   * 区域英文
   */
  private String areaEng;
  /**
   *负责人
   */
  private String principal;
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

  public Long getAreaId() {
    return areaId;
  }

  public void setAreaId(Long areaId) {
    this.areaId = areaId;
  }

  public Long getAreaNumber() {
    return areaNumber;
  }

  public void setAreaNumber(Long areaNumber) {
    this.areaNumber = areaNumber;
  }

  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }

  public String getAreaEng() {
    return areaEng;
  }

  public void setAreaEng(String areaEng) {
    this.areaEng = areaEng;
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
