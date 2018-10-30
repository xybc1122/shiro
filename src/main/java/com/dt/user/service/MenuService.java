package com.dt.user.service;
import java.util.Set;

public interface MenuService {
    /**
     * 查询获得权限列表
     * @param uid
     * @return
     */
    Set<String> findByPermsMenuService(Long uid);
}
