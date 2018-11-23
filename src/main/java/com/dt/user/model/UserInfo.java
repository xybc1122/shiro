package com.dt.user.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户实体类
 */
@JsonIgnoreProperties(value = {"handler"})
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
     * 状态,默认为0，普通用户，1为超级管理员
     */
    private Integer status;

    /**
     * 账户状态，被锁定之类的，默认为0，表示正常
     */
    private int accountStatus;
    /**
     * 创建人员id
     */
    private Long createIdUser;
    /**
     * 更新人id
     */
    private String upIdUser;
    /**
     * 创建日期
     */
    private Long createDate;
    /**
     * 修改时间
     */
    private Long upDate;
    /**
     * 用户有效时间
     */
    private Long effectiveDate;
    /**
     * 密码状态 0为始终有效  非0密码到期会提示修改密码
     */
    private Long pwdStatus;
    /**
     * 用户是否始终有效
     */
    private String userStatus;
    //用户名
    private String name;

    //role对象
    private List<Role> roles;

    //角色名称
    private String rName;

    //登陆时间
    private Long landingTime;

    //手机号码
    private String mobilePhone;

    //菜单名称
    private String menuName;

    //是否删除标示
    private Integer delUser;
    //删除的时间
    private Long delDate;
    //恢复时记录的时间
    private Long restoreDate;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

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

    public Long getCreateIdUser() {
        return createIdUser;
    }

    public void setCreateIdUser(Long createIdUser) {
        this.createIdUser = createIdUser;
    }

    public String getUpIdUser() {
        return upIdUser;
    }

    public void setUpIdUser(String upIdUser) {
        this.upIdUser = upIdUser;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getUpDate() {
        return upDate;
    }

    public void setUpDate(Long upDate) {
        this.upDate = upDate;
    }

    public Long getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Long effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Long getPwdStatus() {
        return pwdStatus;
    }

    public void setPwdStatus(Long pwdStatus) {
        this.pwdStatus = pwdStatus;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Long getLandingTime() {
        return landingTime;
    }

    public void setLandingTime(Long landingTime) {
        this.landingTime = landingTime;
    }

    public Integer getDelUser() {
        return delUser;
    }

    public void setDelUser(Integer delUser) {
        this.delUser = delUser;
    }

    public Long getDelDate() {
        return delDate;
    }

    public void setDelDate(Long delDate) {
        this.delDate = delDate;
    }

    public Long getRestoreDate() {
        return restoreDate;
    }

    public void setRestoreDate(Long restoreDate) {
        this.restoreDate = restoreDate;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid=" + uid +
                ", userName='" + userName + '\'' +
                ", pwd='" + pwd + '\'' +
                ", status=" + status +
                ", accountStatus=" + accountStatus +
                ", createIdUser='" + createIdUser + '\'' +
                ", upIdUser='" + upIdUser + '\'' +
                ", createDate=" + createDate +
                ", upDate=" + upDate +
                ", effectiveDate=" + effectiveDate +
                ", pwdStatus=" + pwdStatus +
                ", userStatus='" + userStatus + '\'' +
                ", name='" + name + '\'' +
                ", roles=" + roles +
                ", rName='" + rName + '\'' +
                '}';
    }
}
