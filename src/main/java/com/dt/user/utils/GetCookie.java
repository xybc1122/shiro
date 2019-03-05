package com.dt.user.utils;

import com.dt.user.model.UserInfo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class GetCookie {

    /**
     * 获得cookie里面的RememberMe
     *
     * @param request
     * @return
     */
    public static String getName(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String name = "";
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            switch (cookie.getName()) {
                case "name":
                    name = cookie.getValue();
                    break;
                default:
                    break;
            }
        }
        return name;
    }
    /**
     * 获得cookie里面的token
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = "";
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            switch (cookie.getName()) {
                case "token":
                    token = cookie.getValue();
                    break;
                default:
                    break;
            }
        }
        return token;
    }

    public static UserInfo getUser(HttpServletRequest request) {
        String token = GetCookie.getToken(request);
        UserInfo user = JwtUtils.jwtUser(token);
        if (user == null) {
            return null;
        }
        return user;
    }
}