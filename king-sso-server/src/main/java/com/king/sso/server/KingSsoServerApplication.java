package com.king.sso.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Auther: wangjinsheng
 * @Date: 2018/7/25 11:55
 * @Description:
 */
@EnableAutoConfiguration
@SpringBootApplication
public class KingSsoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingSsoServerApplication.class, args);
    }
}
