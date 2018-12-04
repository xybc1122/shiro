package com.dt.user.service;
import java.util.Set;

public interface PermsMapperService {


    /**
     * 查询获得权限列表
     *
     * @return
     */
    Set<String> findByPerms(Long uid);
}
