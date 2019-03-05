package com.dt.user.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
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
    private Integer accountStatus;
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
     * userExpirationDate
     * 用户有效时间
     */
    private Long userExpirationDate;
    /**
     * 密码有效期 0为始终有效  非0密码到期会提示修改密码
     */
    private Long pwdValidityPeriod;
    /**
     * 用户是否始终有效
     */
    private String userStatus;
    /**
     * 版本标识
     */
    private Integer version;
    //用户名
    private String name;

    //role对象
    private List<Role> roles;

    //角色名称
    private String rName;

    //多个角色id 拼接  已,号隔开
    private String rId;
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

    //首次登陆是否修改密码
    private Boolean isFirstLogin;
    /**
     * 用户ids
     */
    private String uIds;

    //菜单type 分别 是菜单还是快捷按钮

    private Integer type;
    /**
     * 计算机名
     */
    private String computerName;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(Boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

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

    public Long getUserExpirationDate() {
        return userExpirationDate;
    }

    public void setUserExpirationDate(Long userExpirationDate) {
        this.userExpirationDate = userExpirationDate;
    }

    public Long getPwdValidityPeriod() {
        return pwdValidityPeriod;
    }

    public void setPwdValidityPeriod(Long pwdValidityPeriod) {
        this.pwdValidityPeriod = pwdValidityPeriod;
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

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
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

    public String getuIds() {
        return uIds;
    }

    public void setuIds(String uIds) {
        this.uIds = uIds;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid=" + uid +
                ", userName='" + userName + '\'' +
                ", pwd='" + pwd + '\'' +
                ", status=" + status +
                ", accountStatus=" + accountStatus +
                ", createIdUser=" + createIdUser +
                ", upIdUser='" + upIdUser + '\'' +
                ", createDate=" + createDate +
                ", upDate=" + upDate +
                ", userExpirationDate=" + userExpirationDate +
                ", pwdValidityPeriod=" + pwdValidityPeriod +
                ", userStatus='" + userStatus + '\'' +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", roles=" + roles +
                ", rName='" + rName + '\'' +
                ", rId='" + rId + '\'' +
                ", landingTime=" + landingTime +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", menuName='" + menuName + '\'' +
                ", delUser=" + delUser +
                ", delDate=" + delDate +
                ", restoreDate=" + restoreDate +
                ", isFirstLogin=" + isFirstLogin +
                ", uIds='" + uIds + '\'' +
                ", type=" + type +
                ", computerName='" + computerName + '\'' +
                '}';
    }
}
