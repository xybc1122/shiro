package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {


    @CrossOrigin
    @RequestMapping("/error/user")
    public ResponseBase wrong()
    {
        return BaseApiService.setResultError("您还没登陆!");
    }


    @CrossOrigin
    @RequestMapping("/error/403")
    public ResponseBase error403()
    {
        return BaseApiService.setResultError("403!");
    }
}
