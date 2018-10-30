package com.dt.user.model;

/**
 * 用户关联角色实体类
 */
public class UserRole {
  /**
   * user_info  id
   */
  private Long uId;
  /**
   * role id
   */
  private Long rId;
  /**
   * id
   */
  private Long id;

  public Long getuId() {
    return uId;
  }

  public void setuId(Long uId) {
    this.uId = uId;
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
