package com.dt.user.model.BasePublicModel;

import com.dt.user.model.ParentSysTemLog;

/**
 * 计量单位
 */
public class BasicPublicUnit extends ParentSysTemLog {

  private Long unitId;
  private Long unitNumber;
  private String unitName;
  private String unitEng;
  private String principal;


  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public Long getUnitNumber() {
    return unitNumber;
  }

  public void setUnitNumber(Long unitNumber) {
    this.unitNumber = unitNumber;
  }

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  public String getUnitEng() {
    return unitEng;
  }

  public void setUnitEng(String unitEng) {
    this.unitEng = unitEng;
  }

  public String getPrincipal() {
    return principal;
  }

  public void setPrincipal(String principal) {
    this.principal = principal;
  }

}
