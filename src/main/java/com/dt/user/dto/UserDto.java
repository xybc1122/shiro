package com.dt.user.dto;

import com.dt.user.model.UserInfo;

import java.util.List;

public class UserDto extends UserInfo {

    private Integer currentPage;

    private Integer pageSize;

    private boolean rememberMe; //记住我
    //密码始终有效
    private boolean pwdAlways;
    //用户始终有效
    private boolean uAlways;
    /**
     * 登陆时间范围查询变量
     */
    private List<Long> landingTimes;
    /**
     * 用户有效时间范围查询变量
     */
    private List<Long> userExpirationDates;
    /**
     * 密码有效期范围查询变量
     */
    private List<Long> pwdValidityPeriods;
    /**
     * 创建时间范围查询变量
     */
    private List<Long> createDates;

    public List<Long> getCreateDates() {
        return createDates;
    }

    public void setCreateDates(List<Long> createDates) {
        this.createDates = createDates;
    }

    public List<Long> getLandingTimes() {
        return landingTimes;
    }

    public List<Long> getUserExpirationDates() {
        return userExpirationDates;
    }

    public void setUserExpirationDates(List<Long> userExpirationDates) {
        this.userExpirationDates = userExpirationDates;
    }

    public List<Long> getPwdValidityPeriods() {
        return pwdValidityPeriods;
    }

    public void setPwdValidityPeriods(List<Long> pwdValidityPeriods) {
        this.pwdValidityPeriods = pwdValidityPeriods;
    }

    public void setLandingTimes(List<Long> landingTimes) {
        this.landingTimes = landingTimes;
    }

    public boolean isPwdAlways() {
        return pwdAlways;
    }

    public void setPwdAlways(boolean pwdAlways) {
        this.pwdAlways = pwdAlways;
    }

    public boolean isuAlways() {
        return uAlways;
    }

    public void setuAlways(boolean uAlways) {
        this.uAlways = uAlways;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
