package com.example.dg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ServletComponentScan(basePackages = {"com.example.dg"})
public class DGApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DGApplication.class, args);
    }
}
