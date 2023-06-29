package com.trainlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.trainlab")
public class BackendDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendDevApplication.class, args);
        System.out.println("Hello world!");
    }

}
