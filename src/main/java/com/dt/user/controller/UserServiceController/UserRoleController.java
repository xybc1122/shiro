package com.dt.user.controller.UserServiceController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.UserRole;
import com.dt.user.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/ur")
@RestController
public class UserRoleController {

    @Autowired
    private UserRoleService roleService;

    /**
     * 删除角色信息
     *
     * @param delMap
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/delRole")
    public ResponseBase delRole(@RequestBody Map<String, Object> delMap) {
        if (delMap.get("rolesId") instanceof List) {
            List<Integer> rid = (List<Integer>) delMap.get("rolesId");
            Integer uid = (Integer) delMap.get("uid");
            for (Integer r : rid) {
                roleService.delUserRole(r.longValue(), uid.longValue());
            }
            return BaseApiService.setResultSuccess("角色删除成功~");
        } else if (delMap.get("rolesId") instanceof String) {
            String rid = (String) delMap.get("rolesId");
            List<Integer> uid = (List<Integer>) delMap.get("uid");
            for (Integer u : uid) {
                roleService.delUserRole(Long.parseLong(rid), u.longValue());
            }
            return BaseApiService.setResultSuccess("删除用户成功~");
        }
        return BaseApiService.setResultError("删除用户失败~");
    }

    /**
     * 增加角色信息
     *
     * @param addMap
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/addRole")
    public ResponseBase addRole(@RequestBody Map<String, Object> addMap) {
        UserRole userRole = new UserRole();
        List<UserRole> urList = new ArrayList<>();
        //如果是List类型
        if (addMap.get("rolesId") instanceof List) {
            List<Integer> rolesId = (List<Integer>) addMap.get("rolesId");
            Integer uid = (Integer) addMap.get("uid");
            userRole.setuId(uid.longValue());
            userRole.setrIds(rolesId);
            urList.add(userRole);
            //新增角色信息
            roleService.addUserRole(urList);
            return BaseApiService.setResultSuccess("添加角色成功~");
        } else if (addMap.get("rolesId") instanceof String) {
            //如果是String 类型
            String rId = (String) addMap.get("rolesId");
            List<Integer> uid = (List<Integer>) addMap.get("uid");
            userRole.setuIds(uid);
            userRole.setrId(Long.parseLong(rId));
            urList.add(userRole);
            //新增角色信息
            roleService.addUserRole(urList);

            return BaseApiService.setResultSuccess("添加用户成功~");
        }
        return BaseApiService.setResultError("添加失败~");
    }

}