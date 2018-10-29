package com.dt.user.service;



import java.util.Set;

public interface RoleService {
    //查询角色信息
    Set<String> getAllRolesByUid(Long uid);
}
