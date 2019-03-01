package com.dt.user.model.BasePublicModel;

import com.dt.user.model.ParentSysTemLog;

/**
 * 运输性质
 */
public class BasicLogisticsmgtTransportProperty extends ParentSysTemLog {

  private Long transportPropertyId;
  private Long transportPropertyNumber;
  private String transportPropertyName;
  private String principal;

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

}
