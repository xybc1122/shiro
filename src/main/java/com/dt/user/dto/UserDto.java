package com.dt.user.dto;

import com.dt.user.model.UserInfo;

public class UserDto extends UserInfo {

    private Integer currentPage;
    private Integer pageSize;

    private boolean checked; //记住我

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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
