package com.dt.user.controller.UserServiceController;

import com.alibaba.fastjson.JSONObject;
import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.UserDto;
import com.dt.user.model.UserInfo;
import com.dt.user.service.UserService;
import com.dt.user.shiro.ShiroUtils;
import com.dt.user.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class LoginController extends BaseApiService {

    @Autowired
    private UserService userService;

    /**
     * 登陆
     * @param userDto
     * @return
     */
    @ResponseBody
    @PostMapping("/ajaxLogin")
    public ResponseBase login(@RequestBody UserDto userDto) {
        //获得shiro Subject对象
        Subject currentUser = SecurityUtils.getSubject();
        // dataUserJSON
        JSONObject dataUserJson;
        // 把用户名和密码封装为 UsernamePasswordToken 对象 记住我
        UsernamePasswordToken token = new UsernamePasswordToken(userDto.getUserName(), userDto.getPwd());
        try {
            if (!currentUser.isAuthenticated()) {
                // 执行登录.
                currentUser.login(token);
                UserInfo user = (UserInfo) SecurityUtils.getSubject().getPrincipal();
                //设置 JwtToken
                String userToken = JwtUtils.genJsonWebToken(user);
                user.setPwd(null);
                user.setLandingTime(new Date().getTime());
                //更新登陆时间
                userService.upUserLandingTime(user);
                dataUserJson = new JSONObject();
                dataUserJson.put("user", user);
                dataUserJson.put("token", userToken);
                //设置token到redis 保留7天的时间
                baseRedisService.setString(user.getUserName(), dataUserJson.toString(), 24 * 60 * 7L);
                return BaseApiService.setResultSuccess(dataUserJson);
            } else {
                return BaseApiService.setResultError("已登录~");
            }

        } catch (IncorrectCredentialsException ie) {
            return BaseApiService.setResultError("账号或者密码错误!");

        } catch (AuthenticationException ae) {
            return BaseApiService.setResultError(ae.getMessage());
        }
    }

    /**
     * 退出
     * @return
     */
    @ResponseBody
    @GetMapping("/logout")
    public ResponseBase logout() {
        ShiroUtils.logout();
        return BaseApiService.setResultSuccess("注销成功!");
    }
}
