package com.dt.user.service.impl;

import javax.annotation.Resource;

import com.dt.user.mapper.UserRoleMapper;
import com.dt.user.model.UserRole;
import com.dt.user.service.UserRoleService;
import org.springframework.stereotype.Service;


@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public int addUserRole(UserRole userRoel) {
        return userRoleMapper.addUserRole(userRoel);
    }

}
