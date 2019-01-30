package com.dt.user.service;


import com.dt.user.model.UserRole;

import java.util.List;

public interface UserRoleService {

    /**
     * 新增角色信息
     *
     * @return
     */
    int addUserRole(List<UserRole> urList);
}
