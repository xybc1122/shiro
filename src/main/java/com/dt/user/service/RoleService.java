package com.dt.user.service;




import com.dt.user.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    //查询角色信息
    Set<String> getAllRolesByUid(Long uid);

    List<Role> getRoleList();
}
