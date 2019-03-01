package com.sample;

import com.dt.user.model.ParentSysTemLog;

/**
 * 异常类型
 */
public class BasicLogisticsmgtTransportAbnormalType extends ParentSysTemLog {

  private Long transportAbnormalTypeId;
  private Long transportAbnormalTypeNumber;
  private String transportAbnormalTypeName;
  private String principal;


  public Long getTransportAbnormalTypeId() {
    return transportAbnormalTypeId;
  }

  public void setTransportAbnormalTypeId(Long transportAbnormalTypeId) {
    this.transportAbnormalTypeId = transportAbnormalTypeId;
  }

  public Long getTransportAbnormalTypeNumber() {
    return transportAbnormalTypeNumber;
  }

  public void setTransportAbnormalTypeNumber(Long transportAbnormalTypeNumber) {
    this.transportAbnormalTypeNumber = transportAbnormalTypeNumber;
  }

  public String getTransportAbnormalTypeName() {
    return transportAbnormalTypeName;
  }

  public void setTransportAbnormalTypeName(String transportAbnormalTypeName) {
    this.transportAbnormalTypeName = transportAbnormalTypeName;
  }

  public String getPrincipal() {
    return principal;
  }

  public void setPrincipal(String principal) {
    this.principal = principal;
  }

}
