//package com.example.dg.service.impl;
//
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.example.dg.pojo.entity.Admin;
//import com.example.dg.common.core.domain.entry.User;
//import com.example.dg.mapper.AdminMapper;
//import com.example.dg.mapper.UserMapper;
//import com.example.dg.common.core.domain.model.LoginUser;
//import com.example.dg.service.AdminService;
//import com.example.dg.redis.RedisCache;
//import com.example.dg.service.common.VerificationCodeService;
//import com.example.dg.result.Result;
//import com.example.dg.utils.TokenUtil;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.HashMap;
//import java.util.Objects;
//
///**
//* @author 26030
//* @description 针对表【admin】的数据库操作Service实现
//* @createDate 2025-09-14 22:04:19
//*/
//@Service
//public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
//    implements AdminService{
//    @Resource
//    private UserMapper userMapper;
//    @Resource
//    private RedisCache redisCache;
//    @Resource
//    private AuthenticationManager authenticationManager;
//    @Resource
//    private VerificationCodeService verificationCodeService;
//
//    public Result login(User user) {
//        if (user.getUsername() == null) {
//            user.setUsername(userMapper.selectUserByPhone(user.getPhone()).getUsername());
//        }
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
//        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//        if (Objects.isNull(authenticate)) {
//            throw new RuntimeException("登录失败");
//        }
//        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
//
//        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
//        String userid = loginUser.getUser().getUserId().toString();
//
//        HashMap<String, Object> claims = new HashMap<>();
//        claims.put("userId", userid);
//        String jwt = TokenUtil.createJWT(claims);
//
//        //把完整的用户信息存入redis  userid作为key
//        redisCache.setCacheObject("login:" + userid, loginUser);
//        Result success = Result.success("200", "登录成功");
//        success.add("token", jwt);
//        return success;
//    }
//}
//
//
//
//
