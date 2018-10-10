package com.shiro.demoshiro.shiro;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SsoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("----------------->init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("----------------->doFilter");
        HttpServletRequest request =(HttpServletRequest)servletRequest;
        HttpServletResponse response =(HttpServletResponse) servletResponse;
        System.out.println("request:"+request.getContextPath());
        System.out.println("response:" +response.getHeaderNames());
    }

    @Override
    public void destroy() {
        System.out.println("----------------->destroy");
    }
}
