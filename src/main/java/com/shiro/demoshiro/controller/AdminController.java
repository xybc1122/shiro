package com.shiro.demoshiro.controller;

import com.shiro.demoshiro.config.BaseApiService;
import com.shiro.demoshiro.config.ResponseBase;
import com.shiro.demoshiro.shiro.ShiroUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/admin")
@RestController
public class AdminController {


    @RequestMapping("/add")
    public ResponseBase login() {
        return BaseApiService.setResultSuccess("我来到了Admin页面----"+ShiroUtils.getUserId());
    }
}
