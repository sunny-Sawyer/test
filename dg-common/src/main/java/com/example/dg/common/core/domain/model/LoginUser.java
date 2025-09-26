package com.example.dg.common.core.domain.model;


import com.alibaba.fastjson2.annotation.JSONField;
import com.example.dg.common.core.domain.entry.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUser implements UserDetails {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long villageId;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
//    private String ipaddr;

    /**
     * 登录地点
     */
//    private String loginLocation;

    /**
     * 浏览器类型
     */
//    private String browser;

    /**
     * 操作系统
     */
//    private String os;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 用户信息
     */
    private User user;

//    @JsonIgnore
//    private List<SimpleGrantedAuthority> authorities;

    public LoginUser(User user) {
        this.user = user;
    }

    public LoginUser(User user, Set<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    public LoginUser(Long userId, Long villageId, User user, Set<String> permissions)
    {
        this.userId = userId;
        this.villageId = villageId;
        this.user = user;
        this.permissions = permissions;
    }


    @JsonIgnore
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        if (authorities != null) {
//            return authorities;
//        }
//        //把permissions中String类型的权限信息封装成SimpleGrantedAuthority对象
//        authorities = new ArrayList<>();
//        if (permissions == null) {
//            return authorities;
//        }
//        for (String permission : permissions) {
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
//            authorities.add(authority);
//        }
////        authorities = permissions.stream()
////                .map(SimpleGrantedAuthority::new)
////                .collect(Collectors.toList());
//        return authorities;
        return null;
    }
}