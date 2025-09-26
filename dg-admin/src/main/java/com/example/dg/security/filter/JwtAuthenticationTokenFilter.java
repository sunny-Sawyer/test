package com.example.dg.security.filter;

import com.example.dg.common.core.domain.model.LoginUser;
import com.example.dg.common.core.redis.RedisCache;
import com.example.dg.service.impl.TokenService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisCache redisCache;

    @Resource
    private TokenService tokenService;


//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        //获取token
//        String token = request.getHeader("token");
//        if (!StringUtils.hasText(token)) {
//            //放行
//            filterChain.doFilter(request, response);
//            return;
//        }
//        //解析token
//        String userid;
//        try {
//            Claims claims = TokenUtil.parseJWT(token);
////            userid = claims.getSubject();
//            userid = (String) claims.get("userId");
//        } catch (Exception e) {
//            Result result = Result.error("401", "登录过期");
//            String json = JSON.toJSONString(result);
//            WebUtils.renderString(response, json);
//            return;
//        }
//        //从redis中获取用户信息
//        String redisKey = "login:" + userid;
//        LoginUser loginUser = redisCache.getCacheObject(redisKey);
//        if (Objects.isNull(loginUser)) {
//            Result result = Result.error("401", "登录过期或用户未登录");
//            String json = JSON.toJSONString(result);
//            WebUtils.renderString(response, json);
//            return;
//        }
//        //存入SecurityContextHolder
//        //TODO 获取权限信息封装到Authentication中
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        //放行
//        filterChain.doFilter(request, response);
//    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (ObjectUtils.isNotEmpty(loginUser) && ObjectUtils.isEmpty(SecurityContextHolder.getContext().getAuthentication()))
        {
            tokenService.verifyToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}