package com.example.dg.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xinxin
 * @version 1.0
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/", "/member/login", "/member/register",
//                        "/doc.html","/webjars/**","/v3/api-docs/**","/sms/sendcode","/verifyAuthCode",
//                        "/member/forgetPassword","/error");
//    }

}
