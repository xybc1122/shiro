package com.dt.user.model;


import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *  用户实体类
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户密码
     */
    private String pwd;
    /**
     * 状态 0:禁用，1:正常'
     */
    private Integer status;
    /**
     * 创建人员id
     */
    private String createIdUser;
    /**
     * 更新人id
     */
    private String upIdUser;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改时间
     */
    private Date upDate;
    /**
     * 用户有效时间
     */
    private Date effectiveDate;
    /**
     * 密码状态 0为始终有效  非0密码到期会提示修改密码
     */
    private Integer pwdStatus;
    /**
     * 用户是否始终有效
     */
    private String userStatus;


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateIdUser() {
        return createIdUser;
    }

    public void setCreateIdUser(String createIdUser) {
        this.createIdUser = createIdUser;
    }

    public String getUpIdUser() {
        return upIdUser;
    }

    public void setUpIdUser(String upIdUser) {
        this.upIdUser = upIdUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpDate() {
        return upDate;
    }

    public void setUpDate(Date upDate) {
        this.upDate = upDate;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Integer getPwdStatus() {
        return pwdStatus;
    }

    public void setPwdStatus(Integer pwdStatus) {
        this.pwdStatus = pwdStatus;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

}
