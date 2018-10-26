package com.shiro.demoshiro.controller;

import com.shiro.demoshiro.config.BaseApiService;
import com.shiro.demoshiro.config.ResponseBase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController {
    @RequestMapping("/show")
    public ResponseBase show() {
        return BaseApiService.setResultSuccess("我来到了Index页面");
    }
}
