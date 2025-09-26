package com.example.dg;

import com.example.dg.common.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * @author xinxin
 * @version 1.0
 */
@SpringBootTest(classes = DGApplication.class)
public class PasswordEncoderTest {

    @Test
    public void test(){
        System.out.println(SecurityUtils.encryptPassword("123123Ze"));
    }
}
