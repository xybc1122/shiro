package com.dt.user.model.BasePublicModel;

/**
 * 地区市
 */
public class BasicPublicProvinceCity {

  private Long cityId;
  private String cityNumber;
  private String cityName;
  private String cityNameEng;
  private String provinceNumber;
  private String remark;
  private Long createDate;
  private Long createIdUser;
  private Long modifyDate;
  private Long modifyIdUser;
  private Long auditDate;
  private Long auditIdUser;


  public long getCityId() {
    return cityId;
  }

  public void setCityId(long cityId) {
    this.cityId = cityId;
  }


  public String getCityNumber() {
    return cityNumber;
  }

  public void setCityNumber(String cityNumber) {
    this.cityNumber = cityNumber;
  }


  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }


  public String getCityNameEng() {
    return cityNameEng;
  }

  public void setCityNameEng(String cityNameEng) {
    this.cityNameEng = cityNameEng;
  }


  public String getProvinceNumber() {
    return provinceNumber;
  }

  public void setProvinceNumber(String provinceNumber) {
    this.provinceNumber = provinceNumber;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


  public long getCreateDate() {
    return createDate;
  }

  public void setCreateDate(long createDate) {
    this.createDate = createDate;
  }


  public long getCreateIdUser() {
    return createIdUser;
  }

  public void setCreateIdUser(long createIdUser) {
    this.createIdUser = createIdUser;
  }


  public long getModifyDate() {
    return modifyDate;
  }

  public void setModifyDate(long modifyDate) {
    this.modifyDate = modifyDate;
  }


  public long getModifyIdUser() {
    return modifyIdUser;
  }

  public void setModifyIdUser(long modifyIdUser) {
    this.modifyIdUser = modifyIdUser;
  }


  public long getAuditDate() {
    return auditDate;
  }

  public void setAuditDate(long auditDate) {
    this.auditDate = auditDate;
  }


  public long getAuditIdUser() {
    return auditIdUser;
  }

  public void setAuditIdUser(long auditIdUser) {
    this.auditIdUser = auditIdUser;
  }

}
