package com.shiro.demoshiro.controller;

import com.shiro.demoshiro.config.BaseApiService;
import com.shiro.demoshiro.config.ResponseBase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {



    @RequestMapping("/error/user")
    public ResponseBase wrong()
    {
        return BaseApiService.setResultError("必须认证授权后再登陆!");
    }
}
