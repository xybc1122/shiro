package com.shiro.demoshiro.doman;

import java.io.Serializable;

public class URole implements Serializable {
  private long id;
  private String name;//角色名称
  private Integer type;//角色类型


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
