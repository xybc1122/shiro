package com.dt.user.dto;

import com.dt.user.model.UserInfo;

public class PageDto extends UserInfo {

    private Integer currentPage;
    private Integer pageSize;

    private boolean rememberMe; //记住我
    //密码始终有效
    private boolean pwdAlways;
    //用户始终有效
    private boolean uAlways;

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
