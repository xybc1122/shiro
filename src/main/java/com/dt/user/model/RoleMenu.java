package com.dt.user.model;

import java.io.Serializable;

/**
 * menu跟role关联实体类
 */
public class RoleMenu  implements Serializable {
  /**
   * menuID
   */
  private Long mId;
  /**
   * roleID
   */
  private Long rId;
  /**
   * id
   */
  private Long id;


  public Long getmId() {
    return mId;
  }

  public void setmId(Long mId) {
    this.mId = mId;
  }

  public Long getrId() {
    return rId;
  }

  public void setrId(Long rId) {
    this.rId = rId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
