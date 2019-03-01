package com.sample;

import com.dt.user.model.ParentSysTemLog;

/**
 * 运输类型
 */
public class BasicLogisticsmgtTransportType extends ParentSysTemLog {

    private Long transportTypeId;
    private Long transportTypeNumber;
    private String transportTypeName;
    private String principal;


    public Long getTransportTypeId() {
        return transportTypeId;
    }

    public void setTransportTypeId(Long transportTypeId) {
        this.transportTypeId = transportTypeId;
    }

    public Long getTransportTypeNumber() {
        return transportTypeNumber;
    }

    public void setTransportTypeNumber(Long transportTypeNumber) {
        this.transportTypeNumber = transportTypeNumber;
    }

    public String getTransportTypeName() {
        return transportTypeName;
    }

    public void setTransportTypeName(String transportTypeName) {
        this.transportTypeName = transportTypeName;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

}
