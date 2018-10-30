package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.shiro.ShiroUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/admin")
@RestController
public class AdminController {


    @GetMapping("/add")
    public ResponseBase login() {
        return BaseApiService.setResultSuccess("我来到了Admin_add页面----"+ShiroUtils.getUserId());
    }

    @RequestMapping("/del")
    public ResponseBase del() {
        return BaseApiService.setResultSuccess("我来到了Admin_del页面----"+ShiroUtils.getUserId());
    }

}
