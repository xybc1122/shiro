package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.UserDto;
import com.dt.user.model.UserInfo;
import com.dt.user.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        PageHelper.startPage(userDto.getPage(), userDto.getSize());
        List<UserInfo> listUser = userService.findByUsers(userDto);
        //获得一些信息
        PageInfo<UserInfo> pageInfo = new PageInfo<>(listUser);
        Map<String, Object> data = new HashMap<>();
        data.put("total_size", pageInfo.getTotal());//总条数
        data.put("total_page", pageInfo.getPages());//总页数
        data.put("current_page", userDto.getPage());//当前页
        data.put("users", pageInfo.getList());//数据
        return BaseApiService.setResultSuccess(data);
    }

}
