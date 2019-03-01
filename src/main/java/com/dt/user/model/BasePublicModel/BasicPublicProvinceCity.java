package com.dt.user.model.BasePublicModel;

import com.dt.user.model.ParentSysTemLog;

/**
 * 地区市
 */
public class BasicPublicProvinceCity extends ParentSysTemLog {

  private Long cityId;
  private String cityNumber;
  private String cityName;
  private String cityNameEng;
  private String provinceNumber;


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


}
