package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.BaseRedisService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.UserDto;
import com.dt.user.model.UserInfo;
import com.dt.user.service.UserService;
import com.dt.user.utils.GetCookie;
import com.dt.user.utils.JwtUtils;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/user/")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public ResponseBase index() {
        return BaseApiService.setResultSuccess("我来到了用户页面");
    }


    @PostMapping("/show")
    public ResponseBase showUsers(@RequestBody UserDto userDto) {
        PageHelper.startPage(userDto.getCurrentPage(), userDto.getPageSize());
        List<UserInfo> listUser = userService.findByUsers(userDto);
        //获得一些信息
        PageInfo<UserInfo> pageInfo = new PageInfo<>(listUser);
        Integer currentPage = userDto.getCurrentPage();
        return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
    }

    //shiro权限控制
    @Transactional //事物
    @RequiresPermissions("sys:user:up")
    @PostMapping("/upUserInfo")
    public ResponseBase userInfoUp(@RequestBody Map<String, Object> mapUser) {
        userService.upUser(mapUser);
        userService.upStaff(mapUser);
        return BaseApiService.setResultSuccess();
    }

    @GetMapping("/getUser")
    public ResponseBase getUser(HttpServletRequest request) {
        String token = GetCookie.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            UserInfo user = JwtUtils.jwtUser(token);
            if (user != null) {
                UserInfo userInfo = userService.getSingleUser(user.getUid());
                return BaseApiService.setResultSuccess(userInfo);
            }
        }
        return BaseApiService.setResultError("token无效~~");
    }


    @PostMapping("/getRoles")
    public ResponseBase getRoles(@RequestBody UserDto userDto) {
        List<UserInfo> listRoles = userService.findByRoleInfo(userDto);
        PageInfo<UserInfo> pageInfo = new PageInfo<>(listRoles);
        Integer currentPage = userDto.getCurrentPage();
        return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
    }
}
