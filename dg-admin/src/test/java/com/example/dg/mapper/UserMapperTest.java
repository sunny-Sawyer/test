package com.example.dg.mapper;

import com.example.dg.DGApplication;
import com.example.dg.common.core.domain.entry.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author xinxin
 * @version 1.0
 */
@SpringBootTest(classes = DGApplication.class)
class UserMapperTest {
    @Resource
    private UserMapper userMapper;

    @Test
    void selectUserById() {
        User user = userMapper.selectUserById(1);
        System.out.println(user);
    }

    @Test
    void insertUser(){
        User user = new User(null);
        user.setVillageId(100L);
        user.setPassword("123456");
        user.setUserName("guyang");
        user.setNickName("孤阳");
//        user.setRoleId(0);
        System.out.println(userMapper.insertUser(user));
    }
}