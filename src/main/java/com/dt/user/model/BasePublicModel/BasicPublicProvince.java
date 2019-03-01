package com.dt.user.model.BasePublicModel;

import com.dt.user.model.ParentSysTemLog;

/**
 * 省 洲
 */
public class BasicPublicProvince extends ParentSysTemLog {

  private Long provinceId;
  private Long provinceNumber;
  private String provinceName;
  private String provinceNameEng;
  private Long siteId;

  public Long getProvinceId() {
    return provinceId;
  }

  public void setProvinceId(Long provinceId) {
    this.provinceId = provinceId;
  }

  public Long getProvinceNumber() {
    return provinceNumber;
  }

  public void setProvinceNumber(Long provinceNumber) {
    this.provinceNumber = provinceNumber;
  }

  public String getProvinceName() {
    return provinceName;
  }

  public void setProvinceName(String provinceName) {
    this.provinceName = provinceName;
  }

  public String getProvinceNameEng() {
    return provinceNameEng;
  }

  public void setProvinceNameEng(String provinceNameEng) {
    this.provinceNameEng = provinceNameEng;
  }

  public Long getSiteId() {
    return siteId;
  }

  public void setSiteId(Long siteId) {
    this.siteId = siteId;
  }

}
