package com.trainlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages = "com.trainlab")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableWebMvc
@ConfigurationPropertiesScan
@EnableCaching
public class BackendDevApplication {

    public static void main(String[] args) {

        SpringApplication.run(BackendDevApplication.class, args);
    }
}