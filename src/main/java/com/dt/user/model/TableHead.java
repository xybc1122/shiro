package com.dt.user.model;

import java.io.Serializable;

public class TableHead implements Serializable {

  private Long id;
  private String headName;
  private String menuId;
  private String topType;
  /**
   * 菜单名称
   */
  private String name ;
  /**
   * 接收ids
   */
  private String ids;
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIds() {
    return ids;
  }

  public void setIds(String ids) {
    this.ids = ids;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getHeadName() {
    return headName;
  }

  public void setHeadName(String headName) {
    this.headName = headName;
  }


  public String getMenuId() {
    return menuId;
  }

  public void setMenuId(String menuId) {
    this.menuId = menuId;
  }

  public String getTopType() {
    return topType;
  }

  public void setTopType(String topType) {
    this.topType = topType;
  }
}
