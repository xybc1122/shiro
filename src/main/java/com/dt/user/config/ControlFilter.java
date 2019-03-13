//package com.dt.user.config;
//
//import com.dt.user.utils.JwtUtils;
//import com.google.gson.Gson;
//import io.jsonwebtoken.Claims;
//import org.apache.shiro.subject.Subject;
//import org.apache.shiro.web.filter.AccessControlFilter;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
// * @ClassName ControlFilter  shiro 自定义过滤器
// * Description TODO
// * @Author 陈恩惠
// * @Date 2019/3/12 13:08
// **/
//public class ControlFilter extends AccessControlFilter {
//    private static Gson gson = new Gson();
//
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
////        String token = request.getHeader("token");
////        if (token == null) {
////            //尝试去参数里面获取看看
////            token = request.getParameter("token");
////        }
////        if (token != null) {
////            Claims claims = JwtUtils.checkJWT(token);
////            if (claims != null) {
////                Integer userId = (Integer) claims.get("id");
////                String name = (String) claims.get("name");
////                request.setAttribute("userId", userId);
////                request.setAttribute("name", name);
////                return true;
////            }
////        }
//        //如果没有登陆
//        if (subject.getPrincipal() == null) {
//            sendJsonMessaget(response, BaseApiService.setResultError("请登录"));
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 响应数据给前端
//     *
//     * @param response
//     */
//    public static void sendJsonMessaget(HttpServletResponse response, Object obj) throws IOException {
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter writer = response.getWriter();
//        writer.print(gson.toJson(obj));
//        writer.close();
//        response.flushBuffer();
//    }
//
//}
