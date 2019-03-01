package com.dt.user.model;

import java.io.Serializable;
import java.util.List;

/**
 * 表头信息
 */
public class TableHead implements Serializable {
    /**
     * 自增ID
     */
    private Long id;
    /**
     * 菜单名称
     */
    private String headName;
    /**
     * 菜单ID
     */
    private String menuId;
    /**
     * 类型
     */
    private String topType;
    /**
     * 排序
     */
    private String topOrder;
    /**
     * 是否锁框
     */
    private int isFixed;
    /**
     * 账号状态
     */
    private List<AccountStatusOptions> statusOptions;

    /**
     * 菜单名称
     */
    private String name;
    /**
     * 输入框类型
     */
    private String inputType;
    /**
     * 接收ids
     */
    private String ids;

    public List<AccountStatusOptions> getStatusOptions() {
        return statusOptions;
    }

    public void setStatusOptions(List<AccountStatusOptions> statusOptions) {
        this.statusOptions = statusOptions;
    }

    public String getTopOrder() {
        return topOrder;
    }

    public void setTopOrder(String topOrder) {
        this.topOrder = topOrder;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }


    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getTopType() {
        return topType;
    }

    public void setTopType(String topType) {
        this.topType = topType;
    }

    public int getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(int isFixed) {
        this.isFixed = isFixed;
    }
}
