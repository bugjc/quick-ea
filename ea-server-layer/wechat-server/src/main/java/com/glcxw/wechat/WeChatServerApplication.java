package com.glcxw.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 程序入口
 * @author aoki
 */
@EnableEurekaClient
@SpringBootApplication
public class WeChatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeChatServerApplication.class, args);
    }

}
