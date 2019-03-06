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
@RequestMapping("/api")
public class IndexController {

    /**
     * 测试是否登陆了的接口
     *
     * @return
     */
    @GetMapping("/index")
    public ResponseBase showIndex() {

        //如果能请求这个接口 说明已经登陆  session 还存在
        return BaseApiService.setResultSuccess("ok");

    }
    }
