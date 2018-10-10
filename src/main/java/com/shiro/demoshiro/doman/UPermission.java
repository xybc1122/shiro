package com.shiro.demoshiro.doman;


import java.io.Serializable;

public class UPermission implements Serializable {

  private long id;
  private String url;//url地址
  private String name;//url描述


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
