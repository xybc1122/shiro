package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.Menu;
import com.dt.user.model.UserInfo;
import com.dt.user.service.MenuService;
import com.dt.user.utils.GetCookie;
import com.dt.user.utils.JwtUtils;
import com.dt.user.utils.GetHeaderToken;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 测试是否在登陆的接口
     *
     * @return
     */
    @GetMapping("/index")
    public ResponseBase showIndex() {

        return BaseApiService.setResultSuccess("已经登录!");
    }

    @GetMapping("show")
    public ResponseBase showMenu(HttpServletRequest request) {
        String token = GetCookie.getToken(request);
        System.out.println(token);
        UserInfo user = jwtUser(token);
        List<Menu> MenuList = null;
        if (user != null) {
            MenuList = menuService.queryMenuList(user);
            return BaseApiService.setResultSuccess(MenuList);
        }
        return BaseApiService.setResultError("token无效!");
    }
    //jwt解析
    public UserInfo jwtUser(String token) {
        UserInfo userIfo = null;
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.checkJWT(token);
            if (claims != null) {
                userIfo = new UserInfo();
                Integer id = (Integer) claims.get("id");
                String name = (String) claims.get("name");
                int status = (Integer) claims.get("status");
                userIfo.setUid(id.longValue());
                userIfo.setUserName(name);
                userIfo.setStatus(status);
            }
        }
        return userIfo;
    }
}
