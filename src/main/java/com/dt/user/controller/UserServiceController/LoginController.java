package com.dt.user.controller.UserServiceController;

import com.alibaba.fastjson.JSONObject;
import com.dt.user.config.BaseApiService;
import com.dt.user.config.BaseRedisService;
import com.dt.user.config.ResponseBase;
import com.dt.user.config.ShiroSessionListener;
import com.dt.user.dto.UserDto;
import com.dt.user.model.UserInfo;
import com.dt.user.service.UserService;
import com.dt.user.shiro.ShiroUtils;
import com.dt.user.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class LoginController extends BaseApiService {
    @Autowired
    private UserService userService;

    @Autowired
    private BaseRedisService redisService;
    //并发 hashMap
    private ConcurrentHashMap<String, Integer> hashMap = new ConcurrentHashMap<>();


    /**
     * 每天6点清除 hashMap中的元素
     */
    @Async("executor")
    @Scheduled(cron = "0 0 6 * * ?")
    public void clearHashMap() {
        System.out.println("删除元素");
        hashMap.clear();
    }

    /**
     * 获得在线用户总数
     *
     * @return
     */
    @GetMapping("/uCount")
    public ResponseBase userCount() {
        return BaseApiService.setResultSuccess(redisService.userCount());
    }

    /**
     * 登陆
     *
     * @param userDto
     * @return
     */
    @PostMapping("/ajaxLogin")
    @Transactional
    public ResponseBase login(@RequestBody UserDto userDto) {
        String sessionName = redisService.getStringKey("sId" + userDto.getUserName());
        if (sessionName != null) {
            return BaseApiService.setResultError("账号在线中");
        }
        String userKey = userDto.getUserName() + "error";
        String strRedis = redisService.getStringKey(userKey);
        //如果不等于null
        if (StringUtils.isNotEmpty(strRedis)) {
            Long ttlDate = redisService.getTtl(userKey);
            return BaseApiService.setResultError("账号/或密码错误被锁定/" + ttlDate + "秒后到期!");
        }
        //获得shiro Subject对象
        Subject currentUser = SecurityUtils.getSubject();
        //登陆保存session ID 到redis
        Session session = currentUser.getSession();
        // dataUserJSON
        JSONObject dataUserJson;
        UserInfo user;
        try {
            //判断用户是通过记住我功能自动登录,此时session失效
            if (!currentUser.isAuthenticated()) {
                // 把用户名和密码封装为 UsernamePasswordToken  userDto.isRememberMe()对象 记住我
                UsernamePasswordToken token = new UsernamePasswordToken(userDto.getUserName(), userDto.getPwd(), userDto.isRememberMe());
                // 执行登录.
                currentUser.login(token);
                user = (UserInfo) SecurityUtils.getSubject().getPrincipal();
                //设置 JwtToken
                String userToken = JwtUtils.genJsonWebToken(user);
                user.setPwd(null);
                user.setLandingTime(new Date().getTime());
                //更新登陆时间
                userService.upUserLandingTime(user);
                dataUserJson = new JSONObject();
                dataUserJson.put("user", user);
                dataUserJson.put("token", userToken);
                //登陆成功设置session
                baseRedisService.setString("sId" + user.getUserName(), session.getId(), 30 * 60L);
                //设置token
                baseRedisService.setString(session.getId().toString(), user.getUserName(), 30 * 60L);
                //登陆成功后 删除Map指定元素
                if (hashMap.get(user.getUserName()) != null) {
                    hashMap.entrySet().removeIf(entry -> entry.getKey().equals(user.getUserName()));
                }
                return BaseApiService.setResultSuccess(dataUserJson);
            } else {
                return BaseApiService.setResultSuccess("已登陆");
            }
        } catch (IncorrectCredentialsException | UnknownAccountException ie) {
            return setLockingTime(userDto);
        } catch (AuthenticationException ae) {
            return BaseApiService.setResultError(ae.getMessage());
        }
    }

    public ResponseBase setLockingTime(UserDto userDto) {
        int errorNumber = 0;
        errorNumber++;
        Long lockingTime = null;
        //报错后 先进来看看 这个账号有没有在hashMap里 ---如果里面有 进去
        if (hashMap.get(userDto.getUserName()) != null) {
            hashMap.put(userDto.getUserName(), errorNumber + hashMap.get(userDto.getUserName()));
        } else {
            hashMap.put(userDto.getUserName(), errorNumber);
        }
        if (hashMap.get(userDto.getUserName()) >= 4) {
            switch (hashMap.get(userDto.getUserName())) {
                case 4:
                    lockingTime = 6L * 5;
                    break;
                case 5:
                    lockingTime = 60L * 5;
                    break;
                case 6:
                    lockingTime = 60L * 15;
                    break;
                case 7:
                    lockingTime = 60L * 60 * 24;
                    break;
            }
            redisService.setString(userDto.getUserName() + "error", "error", lockingTime);
            return BaseApiService.setResultError("账号被锁定!" + lockingTime + "秒");
        }
        return BaseApiService.setResultError("账号或密码错误/你还有" + (4 - hashMap.get(userDto.getUserName()) + "次机会"));
    }

    /**
     * 退出
     *
     * @return
     */
    @GetMapping("/logout")
    public ResponseBase logout() {
        ShiroUtils.logout();
        return BaseApiService.setResultSuccess("注销成功!");
    }
}
