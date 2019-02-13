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
    public static String getRememberMe(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String rememberMe = "";
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            switch (cookie.getName()) {
                case "rememberMe":
                    rememberMe = cookie.getValue();
                    break;
                default:
                    break;
            }
        }
        return rememberMe;
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