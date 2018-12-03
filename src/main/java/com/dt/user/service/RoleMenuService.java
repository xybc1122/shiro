package com.dt.user.service;

import com.dt.user.model.RoleMenu;

import java.util.List;

public interface RoleMenuService {


    /**
     * 通过角色id来查询拥有的菜单
     **/
    List<RoleMenu> gerRoleMenus(Long rid);


    /**
     * 添加角色跟菜单
     */
    int addRoleMenu(Long menuId, Long rid);


    /**
     * 删除角色跟菜单
     */
    int delRoleMenu(RoleMenu roleMenu);
}
