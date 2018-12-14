package com.dt.user.utils;

import javax.servlet.http.HttpServletRequest;

public class HttpServletUtils {
    /**
     * 封装是否上ajax请求
     * @param request
     * @return
     */
    public static boolean jsAjax(HttpServletRequest request){
        String header = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(header) ? true:false;
        return isAjax;
    }
}
