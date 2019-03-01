package com.dt.user.model.BasePublicModel;


import com.dt.user.model.ParentSysTemLog;

/**
 * 货运公司
 */
public class BasicLogisticsmgtTransportCompany extends ParentSysTemLog {

    private Long transportCompanyId;
    private Long transportCompanyNumber;
    private String transportCompanyName;
    private String transportCompanyFullName;
    private String contactPerson;
    private String telPhone;
    private String mobile;
    private String eMail;
    private String address;
    private String principal;

    public Long getTransportCompanyId() {
        return transportCompanyId;
    }

    public void setTransportCompanyId(Long transportCompanyId) {
        this.transportCompanyId = transportCompanyId;
    }

    public Long getTransportCompanyNumber() {
        return transportCompanyNumber;
    }

    public void setTransportCompanyNumber(Long transportCompanyNumber) {
        this.transportCompanyNumber = transportCompanyNumber;
    }

    public String getTransportCompanyName() {
        return transportCompanyName;
    }

    public void setTransportCompanyName(String transportCompanyName) {
        this.transportCompanyName = transportCompanyName;
    }

    public String getTransportCompanyFullName() {
        return transportCompanyFullName;
    }

    public void setTransportCompanyFullName(String transportCompanyFullName) {
        this.transportCompanyFullName = transportCompanyFullName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

}
