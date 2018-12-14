package com.dt.user.utils;

import javax.servlet.http.HttpServletRequest;

public class GetHeaderToken {
    /**
     * 获得请求参数里的token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token == null) {
            //尝试去参数里面获取看看
             token = request.getParameter("token");
        }
        return token;
    }
}
