package com.dt.user.model;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户实体类
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
    private String createIdUser;
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

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
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
