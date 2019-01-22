package com.dt.user.controller.UserServiceController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.Menu;
import com.dt.user.model.UserInfo;
import com.dt.user.service.MenuService;
import com.dt.user.utils.GetCookie;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@RestController
@RequestMapping("menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 通过角色id获取菜单树列表
     *
     * @param rid
     * @return
     */
    @GetMapping("/role/menu")
    public ResponseBase roleMenu(@RequestParam("rid") String rid) {
        List<Menu> rootMenu; //父菜单List
        rootMenu = menuService.findQueryByRoleId(Long.parseLong(rid));
        List<Menu> menuList = new ArrayList<>();
        List<Menu> childMenuList = new ArrayList<>();
        //先找到所有一级菜单
        for (int i = 0; i < rootMenu.size(); i++) {
            //如果==0代表父菜单
            if (rootMenu.get(i).getParentId() == 0) {
                menuList.add(rootMenu.get(i));
            } else {
                childMenuList.add(rootMenu.get(i));
            }
        }
        // 为一级菜单设置子菜单 getChild是递归调用的
        for (Menu menu : menuList) {
            menu.setChildMenus(getChild(menu.getMenuId(), childMenuList));
        }
        return BaseApiService.setResultSuccess(menuList);
    }

    /**
     * 获取菜单列表
     *
     * @return
     */
    @GetMapping("show")
    public ResponseBase showMenu(HttpServletRequest request) {
        //获得用户信息
        UserInfo user = GetCookie.getUser(request);
        List<Menu> rootMenu; //父菜单List
        if (user != null) {
            rootMenu = menuService.queryMenuList(user);
            List<Menu> menuList = new ArrayList<>();
            List<Menu> childMenuList = new ArrayList<>();
            //先找到所有一级菜单
            for (int i = 0; i < rootMenu.size(); i++) {
                //如果==0代表父菜单
                if (rootMenu.get(i).getParentId() != null) {
                    if (rootMenu.get(i).getParentId() == 0) {
                        menuList.add(rootMenu.get(i));
                    } else {
                        childMenuList.add(rootMenu.get(i));
                    }
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

    /**
     * 查询所有的菜单列表
     *
     * @return
     */
    @GetMapping("/findMenuList")
    public ResponseBase findMenuList() {
        int page = 1;
        int size = 50;
        PageHelper.startPage(page, size);
        List<Menu> listMenu = menuService.findMenuList();
        //获得一些信息
        PageInfo<Menu> pageInfo = new PageInfo<>(listMenu);
        Map<String, Object> data = new HashMap<>();
        data.put("total_size", pageInfo.getTotal());//总条数
        data.put("total_page", pageInfo.getPages());//总页数
        data.put("current_page", page);//当前页
        data.put("users", pageInfo.getList());//数据
        return BaseApiService.setResultSuccess(data);
    }

    //通过菜单id查询对应的表头信息
}
