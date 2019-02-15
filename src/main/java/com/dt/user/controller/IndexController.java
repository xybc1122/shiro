package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.utils.GetCookie;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class IndexController {
    /**
     * 测试是否登陆了的接口
     *
     * @return
     */
    @GetMapping("/index")
    public ResponseBase showIndex() {
         return BaseApiService.setResultSuccess("ok");

    }
}
