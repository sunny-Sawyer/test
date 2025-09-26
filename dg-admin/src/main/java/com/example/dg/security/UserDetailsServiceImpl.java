package com.example.dg.security;

import com.example.dg.common.core.domain.model.LoginUser;
import com.example.dg.common.exception.ServiceException;
import com.example.dg.common.core.domain.entry.User;
import com.example.dg.service.common.PasswordService;
import com.example.dg.service.UserService;
import com.example.dg.common.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Resource
    private UserService userService;
    @Resource
    private PasswordService passwordService;
//    @Resource
//    private MenuMapper menuMapper;
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
//        User user = userService.selectUserByUserName(username);
        User user = userService.selectUserByPhone(username);
        // 如果没有查询到用户就抛出异常
        if (Objects.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        }

        passwordService.validate(user);

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(User user)
    {
        return new LoginUser(user.getUserId(), user.getVillageId(), user,null);
    }

}
