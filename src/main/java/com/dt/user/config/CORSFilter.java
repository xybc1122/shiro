//package com.dt.user.config;
//
//
//
//import org.apache.shiro.web.servlet.OncePerRequestFilter;
//import org.springframework.stereotype.Component;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class CORSFilter extends OncePerRequestFilter{
//    /**
//     *在ResponseBodyWrapHandler中已处理跨域问题
//     *     但是在shiro验证未通过跳转/unauth时, 因为redirect 重定向会丢失所有请求头，跨域问题重新出现
//     */
//    @Override
//    protected void doFilterInternal(ServletRequest request, ServletResponse response, javax.servlet.FilterChain chain) throws ServletException, IOException {
//        HttpServletResponse res = (HttpServletResponse) request;
//
//        res.setContentType("text/html;charset=UTF-8");
//
//        res.setHeader("Access-Control-Allow-Origin", "*");
//
//        res.setHeader("Access-Control-Allow-Methods", "*");
//
//        res.setHeader("Access-Control-Max-Age", "0");
//
//        res.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
//
//        res.setHeader("Access-Control-Allow-Credentials", "true");
//
//        res.setHeader("XDomainRequestAllowed","1");
//
//        chain.doFilter(request, response);
//    }
//}
