package com.dt.user.model;

public class ParentSysTemLog {


    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 显示的页数
     */
    private Integer pageSize;

    //状态对象
    private SystemLogStatus systemLogStatus;
    /**
     * 状态ID
     */
    private Long statusId;

    public SystemLogStatus getSystemLogStatus() {
        return systemLogStatus;
    }

    public void setSystemLogStatus(SystemLogStatus systemLogStatus) {
        this.systemLogStatus = systemLogStatus;
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

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }
}
