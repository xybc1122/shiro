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

@RequestMapping("/ur")
@RestController
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

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
        List<Integer> rolesId = (List<Integer>) addMap.get("rolesId");
        Integer uid = (Integer) addMap.get("uid");

        return BaseApiService.setResultSuccess("添加用户成功~");

    }
}
