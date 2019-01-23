package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.utils.GetCookie;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class IndexController {
    /**
     * 测试是否登陆了的接口
     *
     * @return
     */
    @GetMapping("/index")
    public ResponseBase showIndex(HttpServletRequest request) {
//        String rememberMe = GetCookie.getRememberMe(request);
//        //如果里面有 //记住我
//        if (StringUtils.isNotEmpty(rememberMe)) {
//            return BaseApiService.setResultSuccess("ok");
//        }
//        return BaseApiService.setResultError("no");
         return BaseApiService.setResultSuccess("ok");

    }
}
