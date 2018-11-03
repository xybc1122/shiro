package com.dt.user.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class GetCookie {


    public static String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = "";
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

}
