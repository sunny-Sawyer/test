//package com.example.museum.filter;
//
//import com.example.museum.entity.User;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * @author xinxin
// * @version 1.0
// */
////@WebFilter(urlPatterns = {"/*"})
//@Slf4j
//public class AuthFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        log.error("走了验证过滤器");
//        String permissionId = httpServletRequest.getParameter("permissionId");
//        if ("1".equals(permissionId)) {
//            log.error("该用户是普通用户");
//            HttpServletResponse resp = (HttpServletResponse) servletResponse;
//            User member = (User) httpServletRequest.getSession().getAttribute("member");
//        } else if ("0".equals(permissionId)) {
//            log.error("该用户是超管");
//            HttpServletResponse resp = (HttpServletResponse) servletResponse;
//            User member = (User) httpServletRequest.getSession().getAttribute("member");
//            System.out.println(member);
//        }
////        if (null == member) {
////            httpServletRequest.getRequestDispatcher("/views/member/login.jsp")
////                    .forward(servletRequest, servletResponse);
////            return;
////        }
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }
//}
