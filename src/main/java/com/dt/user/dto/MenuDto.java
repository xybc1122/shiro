package com.dt.user.dto;


import java.util.*;

public class MenuDto{

    /**
     * 存储List对象数组
     */
    List<Map<String,Object>> menuList;

    public List<Map<String, Object>> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Map<String, Object>> menuList) {
        this.menuList = menuList;
    }
}
