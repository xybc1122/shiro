//package com.dt.user.config;
//
//import org.apache.shiro.subject.Subject;
//import org.apache.shiro.web.filter.AccessControlFilter;
//import org.apache.shiro.web.util.WebUtils;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @ClassName ControlFilter
// * Description TODO
// * @Author 陈恩惠
// * @Date 2019/3/12 13:08
// **/
//public class ControlFilter extends AccessControlFilter {
//    static final String LOGIN_URL = "/error/user";
//
//    @Override
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//        return false;
//    }
//
//    @Override
//    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        Subject subject = getSubject(request, response);
//        //如果没有登陆
//        if (subject.getPrincipal() == null) {
//            saveRequest(request);
//            System.out.println("重定向");
//            response.sendRedirect(LOGIN_URL);
//            return false;
//        }
//        return true;
//    }
//
//
//}
