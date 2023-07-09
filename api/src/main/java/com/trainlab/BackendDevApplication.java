package com.trainlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages = "com.trainlab")
@EnableWebMvc
public class BackendDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendDevApplication.class, args);
    }

}
