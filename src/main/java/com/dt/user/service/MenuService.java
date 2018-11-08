package com.dt.user.service;

import com.dt.user.dto.MenuDto;
import com.dt.user.model.Menu;
import com.dt.user.model.UserInfo;

import java.util.List;
import java.util.Set;

public interface MenuService {
    /**
     * 查询获得shiro权限
     * @param uid
     * @return
     */
    Set<String> findByPermsMenuService(Long uid);
    /**
     * 查找用户所拥有的所有菜单
     */
    List<Menu> queryMenuList(UserInfo userInfo);

    /**
     * 获取菜单表信息
     */
    List<Menu>findMenuList();

}
