package com.dt.user.service.impl;

import com.dt.user.mapper.MenuMapper;
import com.dt.user.model.Menu;
import com.dt.user.model.UserInfo;
import com.dt.user.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

}
