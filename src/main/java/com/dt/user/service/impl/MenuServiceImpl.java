package com.dt.user.service.impl;

import com.dt.user.dto.MenuDto;
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
    public Set<String> findByPermsMenuService(Long uid) {
        //获得用户的权限
        List<String> perms = menuMapper.findByPermsMenu(uid);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotBlank(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public List<Menu> queryMenuList(UserInfo userInfo) {
        //用户所能看到的菜单
        return menuMapper.queryMenuList(userInfo);
    }

    @Override
    public List<MenuDto> findMenuList() {
        List<Menu> listMenu = menuMapper.findMenuList();
        Map<String, Object> map;
        List<Map<String, Object>> arrMenu;
        List<MenuDto> listDto = new ArrayList<>();
        MenuDto mDto;
        int menu_i = 1;
        for (Menu menu : listMenu) {
            int index = listMenu.indexOf(menu);
            if ((index + 1) == menu_i) {
                if (menu.getParentId() == 0) {
                    arrMenu = new ArrayList<>();

                    map = new HashMap<>();
                    map.put("value", menu.getName());
                    arrMenu.add(map);

                    map = new HashMap<>();
                    map.put("value", menu.getMenuOrder());
                    arrMenu.add(map);

                    map = new HashMap<>();
                    map.put("value", menu.getUrl());
                    arrMenu.add(map);

                    map = new HashMap<>();
                    map.put("value", menu.getIcon());
                    arrMenu.add(map);
                    //#####################################分割
                    mDto = new MenuDto();
                    mDto.setMenuList(arrMenu);
                    listDto.add(mDto);
                }
            }
            menu_i++;
        }
        return listDto;
    }

}
