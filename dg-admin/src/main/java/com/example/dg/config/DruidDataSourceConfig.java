package com.example.dg.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xinxin
 * @version 1.0
 */
@Configuration
public class DruidDataSourceConfig {
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DruidDataSource dataSource() {
        return new DruidDataSource();
    }
}
