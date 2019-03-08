package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController

@RequestMapping("/api")
public class ErrorController {

    /**
     * 没有登陆信息
     * @return
     */
    @RequestMapping("/error/user")
    public ResponseBase wrong()
    {

        return BaseApiService.setResultError("您还没登陆!");
    }

}
