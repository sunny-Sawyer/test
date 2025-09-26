package com.example.dg.service;

import com.example.dg.DGApplication;
import com.example.dg.common.core.domain.entry.User;
import com.example.dg.common.core.redis.RedisCache;
import com.example.dg.common.core.domain.model.LoginUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author xinxin
 * @version 1.0
 */
@SpringBootTest(classes = DGApplication.class)
class RedisCacheTest {
    @Resource
    private RedisCache redisCache;

//    @Test
//    void setCacheObject() {
//        User user = new User();
//        user.setUserId(3);
////        user.setUsername("1234");
//        user.setPassword("$2a$10$CmU2J9OaKUTVZR8ZqSoyv.sAa3vVLpHalFUs8krZjFdi/LHosews2");
//        user.setPhone("12311231233");
//        LoginUser loginUser = new LoginUser(user);
//        redisCache.setCacheObject("login:" + user.getUserId(), loginUser);
//        Object test = redisCache.getCacheObject("login:" + user.getUserId());
////        System.out.println(redisService.deleteObject("test"));
//        System.out.println(test);
//    }
}