package com.dt.user.controller.UserServiceController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.UserDto;
import com.dt.user.model.Role;
import com.dt.user.model.UserInfo;
import com.dt.user.service.RoleService;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 查询所有角色信息
     * @return
     */
    @GetMapping("/findByListRoles")
    public ResponseBase findByListRoles() {
        List<Role> roles = roleService.getRoleList();
        return BaseApiService.setResultSuccess(roles);
    }


    /**
     * 查询一个角色下的所有用户跟菜单
     * @param userDto
     * @return
     */
    @PostMapping("/getRoles")
    public ResponseBase getRoles(@RequestBody UserDto userDto) {
        PageHelper.startPage(userDto.getCurrentPage(), userDto.getPageSize());
        List<UserInfo> listRoles = roleService.findByRoleInfo(userDto);
        PageInfo<UserInfo> pageInfo = new PageInfo<>(listRoles);
        Integer currentPage = userDto.getCurrentPage();
        return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
    }
}
