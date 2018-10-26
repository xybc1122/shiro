package com.shiro.demoshiro.doman;

import java.io.Serializable;

public class URole implements Serializable {
  private long rId;
  private String rName;//角色名称
  private Integer types;//角色类型

  public long getrId() {
    return rId;
  }

  public void setrId(long rId) {
    this.rId = rId;
  }

  public String getrName() {
    return rName;
  }

  public void setrName(String rName) {
    this.rName = rName;
  }

  public Integer getTypes() {
    return types;
  }

  public void setTypes(Integer types) {
    this.types = types;
  }
}
