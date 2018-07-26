package com.king.sso.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Auther: king
 * @Date: 2018/7/26 14:00
 * @Description:
 */
@EnableAutoConfiguration
@SpringBootApplication
public class KingClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingClientApplication.class, args);
    }
}
