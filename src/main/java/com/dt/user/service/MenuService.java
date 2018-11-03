package com.dt.user.service;

import com.dt.user.model.Menu;
import com.dt.user.model.UserInfo;

import java.util.List;
import java.util.Set;

public interface MenuService {
    /**
     * 查询获得权限列表
     * @param uid
     * @return
     */
    Set<String> findByPermsMenuService(Long uid);

    List<Menu> queryMenuList(UserInfo userInfo);

}
