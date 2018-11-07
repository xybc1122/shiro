package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.Menu;
import com.dt.user.model.UserInfo;
import com.dt.user.service.MenuService;
import com.dt.user.utils.GetCookie;
import com.dt.user.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 测试是否在登陆的接口
     *
     * @return
     */
    @GetMapping("/index")
    public ResponseBase showIndex() {

        return BaseApiService.setResultSuccess("已经登录!");
    }

    @GetMapping("show")
    public ResponseBase showMenu(HttpServletRequest request) {
        String token = GetCookie.getToken(request);
        UserInfo user = jwtUser(token);
        List<Menu> rootMenu; //父菜单List
        if (user != null) {
            rootMenu = menuService.queryMenuList(user);
            List<Menu> menuList = new ArrayList<>();
            List<Menu> childMenuList = new ArrayList<>();
            //先找到所有一级菜单
            for (int i = 0; i < rootMenu.size(); i++) {
                //如果==0代表父菜单
                if (rootMenu.get(i).getParentId() == 0) {
                    menuList.add(rootMenu.get(i));
                }else{
                    childMenuList.add(rootMenu.get(i));
                }
            }
            // 为一级菜单设置子菜单 getChild是递归调用的
            for (Menu menu : menuList) {
                menu.setChildMenus(getChild(menu.getMenuId(), childMenuList));
            }
            return BaseApiService.setResultSuccess(menuList);
        }
        return BaseApiService.setResultError("token无效!");
    }
    //递归查找子菜单

    private List<Menu> getChild(Long menuId, List<Menu> childMenuList) {
        // 子菜单
        List<Menu> childList = new ArrayList<>();
        for (Menu menu : childMenuList) {
            // 遍历所有节点，将子菜单getParentId与传过来的父menuId比较
            if (menu.getParentId().equals(menuId)) {
                //如果是true 就添加到父菜单下面
                childList.add(menu);
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (Menu childMenu : childList) {// 没有url子菜单还有子菜单
            if (StringUtils.isBlank(childMenu.getUrl())) {
                // 递归
                childMenu.setChildMenus(getChild(childMenu.getMenuId(), childMenuList));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }


    //jwt解析
    public UserInfo jwtUser(String token) {
        UserInfo userIfo = null;
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.checkJWT(token);
            if (claims != null) {
                userIfo = new UserInfo();
                Integer id = (Integer) claims.get("id");
                String name = (String) claims.get("name");
                int status = (Integer) claims.get("status");
                userIfo.setUid(id.longValue());
                userIfo.setUserName(name);
                userIfo.setStatus(status);
            }
        }
        return userIfo;
    }
}
