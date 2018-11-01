package com.dt.user.service.impl;

import com.dt.user.mapper.RolesMapper;

import com.dt.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RolesMapper rolesMapper;

    @Override
    public Set<String> getAllRolesByUid(Long uid) {
        return rolesMapper.getAllRolesByUid(uid);
    }
}