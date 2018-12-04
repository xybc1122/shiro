package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.RoleMenu;
import com.dt.user.service.RoleMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RequestMapping("rm")
@RestController
public class RoleMenuController {

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 角色管理修改页面 点击确定后请求此接口
     * 包含删除菜单，新增菜单
     * @param menuMap
     * @return
     */
    @PostMapping("/upMenus")
    @Transactional
    public ResponseBase getMenus(@RequestBody Map<String, Object> menuMap) {
        RoleMenu roleMenu;
        List<RoleMenu> roleMenuList;
        String rid = (String) menuMap.get("rid");
        String menuIds = (String) menuMap.get("menuIds");
        List<String> arrMenuIds = Arrays.asList(menuIds.split(","));
        Boolean menuFlg = (Boolean) menuMap.get("menuFlg");
        //true =添加菜单  false =删除菜单
        if (menuFlg) {
            //先查询已有的关联菜单
            roleMenuList = roleMenuService.gerRoleMenus(Long.parseLong(rid));
            List<Long> resultMenuIds = menuListRoles(arrMenuIds, roleMenuList);
            if (resultMenuIds.size() != 0) {
                //新增菜单
                for (int i = 0; i < resultMenuIds.size(); i++) {
                    Long menuId = resultMenuIds.get(i);
                    roleMenuService.addRoleMenu(menuId, Long.parseLong(rid));
                }
                return BaseApiService.setResultSuccess("添加菜单成功~");
            }
        } else {
            //判断如果全部为空
            if (StringUtils.isBlank(menuIds)) {
                //通过rid 删除所有的关联的菜单
                roleMenu = new RoleMenu();
                roleMenu.setrId(Long.parseLong(rid));
                roleMenuService.delRoleMenu(roleMenu);
                return BaseApiService.setResultSuccess("全部删除成功~");
            } else {
                roleMenu = new RoleMenu();
                //通过arrMenuIds 删除关联的菜单
                for (int i = 0; i < arrMenuIds.size(); i++) {
                    roleMenu.setrId(Long.parseLong(rid));
                    roleMenu.setmId(Long.parseLong(arrMenuIds.get(i)));
                    roleMenuService.delRoleMenu(roleMenu);
                }
                return BaseApiService.setResultSuccess("删除关联成功~");
            }
        }
        return BaseApiService.setResultError("修改失败~");
    }


    /**
     * 对比两个数组是否是否一直 如果有不一致的 取出不一致的数据
     * @param arrMenuIds
     * @param roleMenuList
     * @return
     */
    public List<Long> menuListRoles(List<String> arrMenuIds, List<RoleMenu> roleMenuList) {
        List<Long> resultMenuIds = new ArrayList<>();
        for (int i = 0; i < arrMenuIds.size(); i++) {
            String arrIds = arrMenuIds.get(i);
            boolean flag = false;
            for (int j = 0; j < roleMenuList.size(); j++) {
                RoleMenu roleMenuIds = roleMenuList.get(j);
                if (arrIds.equals(roleMenuIds.getmId().toString())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                resultMenuIds.add(Long.parseLong(arrIds));
            }
        }
        return resultMenuIds;
    }

}
