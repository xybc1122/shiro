package com.dt.user.controller.UserServiceController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.BaseRedisService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.Menu;
import com.dt.user.model.UserInfo;
import com.dt.user.service.MenuService;
import com.dt.user.utils.GetCookie;
import com.dt.user.utils.UuIDUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private BaseRedisService redisService;

    /**
     * 校验token  菜单是否已更新
     *
     * @return
     */
    @GetMapping("/token/menu")
    public ResponseBase checkMenuToken() {
        //获得redis token
        String tokenRedis = redisService.getStringKey("tokenMenu");
        //如果是空 说明还没更新
        if (StringUtils.isEmpty(tokenRedis)){
            return BaseApiService.setResultError("数据没更新");
        }
        //删除缓存数据
        redisService.delData("tokenMenu");
        return BaseApiService.setResultSuccess("数据已更新");
    }

    /**
     * 菜单修改接口
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/up/menu")
    public ResponseBase roleMenu(@RequestBody Map<String, Object> menuMap) {
        List<LinkedHashMap> linkedMenu = (List<LinkedHashMap>) menuMap.get("newMenu");
        List<Integer> idsMenu = (List<Integer>) menuMap.get("idsMenu");
        if (linkedMenu != null && linkedMenu.size() > 0) {
            //把LinkedHashMap类型转换java Ben
            ObjectMapper mapper = new ObjectMapper();
            List<Menu> menus = mapper.convertValue(linkedMenu, new TypeReference<List<Menu>>() {
            });
            if (menus != null && menus.size() > 0) {
                //插入数据
                int count = menuService.addMenu(menus);
                if (count > 0) {
                    //代表更新
                    redisService.setString("tokenMenu", 0);
                    return BaseApiService.setResultSuccess("新增成功");
                }
            }
            return BaseApiService.setResultError("插入失败");
        }
        if (idsMenu != null && idsMenu.size() > 0) {
            //删除菜单操作
            System.out.println(idsMenu);
        }
        return BaseApiService.setResultError("参数为NUll");
    }

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
    public ResponseBase showMenu(HttpServletRequest request, @RequestParam("type") String type) {
        //获得用户信息
        UserInfo user = GetCookie.getUser(request);
        List<Menu> rootMenu; //父菜单List
        if (user != null) {
            if (type.equals("undefined")) {
                user.setType(0);
            } else {
                user.setType(Integer.parseInt(type));
            }
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
}
