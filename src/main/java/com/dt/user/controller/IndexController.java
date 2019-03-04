package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@RestController
public class IndexController {
    @Autowired
    private DataSource dataSource;
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
    @RequestMapping("/c")
    public String index() throws SQLException {
        System.out.println(dataSource.getConnection());
        System.out.println(dataSource);
        return "hello spring boot";
    }
}
