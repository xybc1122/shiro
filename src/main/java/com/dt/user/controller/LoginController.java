package com.dt.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.UserInfo;
import com.dt.user.shiro.ShiroUtils;
import com.dt.user.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @ResponseBody
    @PostMapping("/ajaxLogin")
    //@RequestParam("userName")String name,@RequestParam("pwd")String pwd
//    @RequestBody Map userMap
    public ResponseBase login(@RequestBody UserInfo userInfo) {
        //获得shiro Subject对象
        Subject currentUser = SecurityUtils.getSubject();

        // dataUserJSON
        JSONObject dataUserJson = null;
        UserInfo user = null;
        // 调动 Subject 的 isAuthenticated()
        if (!currentUser.isAuthenticated()) {
            // 把用户名和密码封装为 UsernamePasswordToken 对象
            UsernamePasswordToken token = new
//            userMap.get("userName").toString(), userMap.get("pwd").toString()
                    UsernamePasswordToken(userInfo.getUserName(), userInfo.getPwd());
            // rememberme   记住我
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
                user.setName(userShiro.getName());
                //设置 JwtToken
                String userToken = JwtUtils.genJsonWebToken(user);
                dataUserJson = new JSONObject();
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

    @ResponseBody
    @GetMapping("/logout")
    public ResponseBase logout() {
        ShiroUtils.logout();
        return BaseApiService.setResultSuccess("注销成功!");
    }
}
