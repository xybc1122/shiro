package com.dt.user.service.impl;

import com.dt.user.mapper.RoleMapper;

import com.dt.user.model.Role;
import com.dt.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper rolesMapper;

    @Override
    public Set<String> getAllRolesByUid(Long uid) {

        return rolesMapper.getAllRolesByUid(uid);
    }

    @Override
    public List<Role> getRoleList() {
        return rolesMapper.getRoleList();
    }
}
