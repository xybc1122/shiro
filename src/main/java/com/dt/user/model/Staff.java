package com.dt.user.model;


import java.io.Serializable;

/**
 * 员工表
 */
public class Staff implements Serializable {

  private Long sId;
  private Long uId;
  private Long ptId;
  private Long mobilePhone;
  private String sName;

  public Long getsId() {
    return sId;
  }

  public void setsId(Long sId) {
    this.sId = sId;
  }

  public Long getuId() {
    return uId;
  }

  public void setuId(Long uId) {
    this.uId = uId;
  }

  public Long getPtId() {
    return ptId;
  }

  public void setPtId(Long ptId) {
    this.ptId = ptId;
  }

  public Long getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(Long mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getsName() {
    return sName;
  }

  public void setsName(String sName) {
    this.sName = sName;
  }
}
