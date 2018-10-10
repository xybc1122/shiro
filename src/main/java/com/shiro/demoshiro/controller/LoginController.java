package com.shiro.demoshiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class LoginController {

    @RequestMapping("/user/login")
    public String login(@RequestParam("username")String  username,@RequestParam("pwd") String pwd) {
        //获得shiro Subject对象
        Subject currentUser = SecurityUtils.getSubject();

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
            }
            catch (AuthenticationException ae) {
              System.out.println("登陆失败!"+ ae.getMessage());

            }
        }

        return "redirect:/success.html";
    }

}
