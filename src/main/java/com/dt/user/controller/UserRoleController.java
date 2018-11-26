package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.UserRole;
import com.dt.user.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/ur")
@RestController
public class UserRoleController {

    @Autowired
    private UserRoleService roleService;

    /**
     *删除角色信息
     * @param delMap
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/delRole")
    public ResponseBase delRole(@RequestBody Map<String, Object> delMap) {
        List<Integer> rid = (List<Integer>) delMap.get("movedKeys");
        Integer uid = (Integer) delMap.get("uid");
        for (Integer r : rid) {
            roleService.delUserRole(r.longValue(),uid.longValue());
        }
        return BaseApiService.setResultSuccess("角色删除成功~");
    }

    /**
     *增加角色信息
     * @param addMap
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/addRole")
    public ResponseBase addRole(@RequestBody Map<String, Object> addMap) {
        List<Integer> rolesId = (List<Integer>) addMap.get("movedKeys");
        Integer uid = (Integer) addMap.get("uid");
        UserRole userRole = new UserRole();
        for (Integer role : rolesId) {
            userRole.setuId(uid.longValue());
            userRole.setrId(role.longValue());
            //新增角色信息
            roleService.addUserRole(userRole);
        }
        return BaseApiService.setResultSuccess("添加角色成功~");
    }

}
