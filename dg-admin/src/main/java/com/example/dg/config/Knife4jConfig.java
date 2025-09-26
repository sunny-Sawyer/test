package com.example.dg.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("数字治理平台 接口文档")
                        .description("项目简介，支持Markdown格式")
                        .version("V1.0")
                        .contact(new Contact().name("廖泽鑫，高泽楷"))
                );
    }
}
