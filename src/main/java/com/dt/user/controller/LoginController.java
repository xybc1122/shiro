package com.dt.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.UserInfo;
import com.dt.user.utils.JwtUtils;
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
    @PostMapping("/ajaxLogin")
    public ResponseBase login(@RequestParam("userName") String userName, @RequestParam("pwd") String pwd) {
        //获得shiro Subject对象
        Subject currentUser = SecurityUtils.getSubject();
        // dataUserJSON
        JSONObject dataUserJson = null;
        UserInfo user = null;
        // Do some stuff with a Session (no need for a web or EJB container!!!)
        // 测试使用 Session
        // 获取 Session: Subject#getSession()
        Session session = currentUser.getSession();
        // 测试当前的用户是否已经被认证. 即是否已经登录.
        // 调动 Subject 的 isAuthenticated()
        if (!currentUser.isAuthenticated()) {
            // 把用户名和密码封装为 UsernamePasswordToken 对象
            UsernamePasswordToken token = new UsernamePasswordToken(userName, pwd);
            // rememberme
            token.setRememberMe(true);
            try {
                // 执行登录.
                currentUser.login(token);
                //不能直直接pull到json里 会报错
                UserInfo userShiro = (UserInfo) SecurityUtils.getSubject().getPrincipal();
                //设置user属性
                user = new UserInfo();
                user.setUid(userShiro.getUid());
                user.setPwd(null);
                user.setUserName(userShiro.getUserName());
                user.setStatus(userShiro.getStatus());
                user.setCreateDate(userShiro.getCreateDate());
                user.setEffectiveDate(userShiro.getCreateDate());
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
