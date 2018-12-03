package com.dt.user.service.impl;

import com.dt.user.mapper.RoleMenuMapper;
import com.dt.user.model.RoleMenu;
import com.dt.user.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<RoleMenu> gerRoleMenus(Long rid) {
        return roleMenuMapper.gerRoleMenus(rid);
    }

    @Override
    public int addRoleMenu(Long menuId, Long rid) {
        return roleMenuMapper.addRoleMenu(menuId, rid);
    }

    @Override
    public int delRoleMenu(RoleMenu roleMenu) {
        return roleMenuMapper.delRoleMenu(roleMenu);
    }
}
