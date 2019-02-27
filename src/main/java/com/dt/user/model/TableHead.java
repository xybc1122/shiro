package com.dt.user.model;

import java.io.Serializable;

public class TableHead implements Serializable {

    private Long id;
    private String headName;
    private String menuId;
    private String topType;
    private int isFixed;
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
