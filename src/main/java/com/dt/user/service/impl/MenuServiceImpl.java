package com.dt.user.service.impl;

import com.dt.user.mapper.MenuMapper;
import com.dt.user.model.Menu;
import com.dt.user.model.UserInfo;
import com.dt.user.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;


    @Override
    public List<Menu> queryMenuList(UserInfo userInfo) {
        //用户所能看到的菜单
        return menuMapper.queryMenuList(userInfo);
    }

    @Override
    public List<Menu> findMenuList() {
        List<Menu> listMenu = menuMapper.findMenuList();
        List<Menu> NewMenu = new ArrayList<>();
        for (Menu menu : listMenu) {
            if (menu.getParentId() == 0) {
                NewMenu.add(menu);
            }
        }
        return NewMenu;
    }

    @Override
    public List<Menu> findQueryByRoleId(Long roleId) {
        return menuMapper.findQueryByRoleId(roleId);
    }
}
