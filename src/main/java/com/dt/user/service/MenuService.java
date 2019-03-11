package com.dt.user.service;

import com.dt.user.model.Menu;
import com.dt.user.model.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface MenuService {

    /**
     * 查找用户所拥有的所有菜单
     */
    List<Menu> queryMenuList(UserInfo userInfo);


    /**
     * 通过角色查询菜单
     *
     * @param roleId
     * @return
     */
    List<Menu> findQueryByRoleId(@Param("roleId") Long roleId);


    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    int addMenu(List<Menu> menu);

    /**
     * 菜单更新
     */
    int upMenu(Menu menu);
}

