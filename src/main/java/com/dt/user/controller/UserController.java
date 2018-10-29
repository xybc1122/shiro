package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user/")
@RestController
public class UserController {


    @RequestMapping("/index")
    public ResponseBase index() {
        return BaseApiService.setResultSuccess("我来到了用户页面");
    }
}
