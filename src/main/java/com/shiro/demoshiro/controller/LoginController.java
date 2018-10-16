package com.shiro.demoshiro.controller;

import com.alibaba.fastjson.JSONObject;
import com.shiro.demoshiro.config.BaseApiService;
import com.shiro.demoshiro.config.ResponseBase;
import com.shiro.demoshiro.doman.User;
import com.shiro.demoshiro.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @ResponseBody
    @PostMapping("/login")
    public ResponseBase login(@RequestParam("username") String username, @RequestParam("pwd") String pwd) {
        //获得shiro Subject对象
        Subject currentUser = SecurityUtils.getSubject();
        // dataUserJSON
        JSONObject dataUserJson = null;
        User user = null;
        // Do some stuff with a Session (no need for a web or EJB container!!!)
        // 测试使用 Session
        // 获取 Session: Subject#getSession()
        Session session = currentUser.getSession();
        // 测试当前的用户是否已经被认证. 即是否已经登录.
        // 调动 Subject 的 isAuthenticated()
        if (!currentUser.isAuthenticated()) {
            // 把用户名和密码封装为 UsernamePasswordToken 对象
            UsernamePasswordToken token = new UsernamePasswordToken(username, pwd);
            // rememberme
            token.setRememberMe(true);
            try {
                // 执行登录.
                currentUser.login(token);
                //不能直直接pull到json里 会报错
                User userShiro = (User) SecurityUtils.getSubject().getPrincipal();
                //设置user属性
                user = new User();
                user.setUid(userShiro.getUid());
                user.setPassword(null);
                user.setRoles(userShiro.getRoles());
                user.setCreatedTime(user.getCreatedTime());
                user.setUsername(userShiro.getUsername());
                //设置 JwtToken
                String userToken = JwtUtils.genJsonWebToken(user);
                dataUserJson=new JSONObject();
                dataUserJson.put("user", user);
                dataUserJson.put("token", userToken);
            } catch (IncorrectCredentialsException ie) {
                return BaseApiService.setResultError("账号或者密码错误!");
            } catch (AuthenticationException ae) {
                return BaseApiService.setResultError(ae.getMessage());

            }
        }
        return BaseApiService.setResultSuccess(dataUserJson);
    }
}
