package com.dt.user.model;

import java.util.List;

/**
 * 用户关联角色实体类
 */
public class UserRole {
    /**
     * user_info  id
     */
    private Long uId;
    /**
     * user IDs
     */
    private List<Integer> uIds;
    /**
     * role id
     */
    private Long rId;

    /**
     * role ids
     */
    private List<Integer> rIds;
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

    public List<Integer> getrIds() {
        return rIds;
    }

    public void setrIds(List<Integer> rIds) {
        this.rIds = rIds;
    }

    public List<Integer> getuIds() {
        return uIds;
    }

    public void setuIds(List<Integer> uIds) {
        this.uIds = uIds;
    }
}
