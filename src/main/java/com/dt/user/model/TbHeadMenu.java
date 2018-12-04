package com.dt.user.model;

import java.io.Serializable;

public class TbHeadMenu implements Serializable {

  private Long id;
  private Long mId;
  private Long thId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getmId() {
    return mId;
  }

  public void setmId(Long mId) {
    this.mId = mId;
  }

  public Long getThId() {
    return thId;
  }

  public void setThId(Long thId) {
    this.thId = thId;
  }
}
