package com.dt.user.service.impl;

import com.dt.user.mapper.UserRoleMapper;
import com.dt.user.model.UserRole;
import com.dt.user.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public int addUserRole(UserRole userRole) {
        return userRoleMapper.addUserRole(userRole);
    }

    @Override
    public int delUserRole(Long rid, Long uid) {
        return userRoleMapper.delUserRole(rid, uid);
    }

}
