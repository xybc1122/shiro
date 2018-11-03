package com.dt.user.utils;

import javax.servlet.http.HttpServletRequest;

public class GetHeaderToken {

    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token == null) {
            //尝试去参数里面获取看看
             token = request.getParameter("token");
        }
        return token;
    }
}
