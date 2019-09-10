package com.bugjc.ea.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 程序入口
 * @author qingyang
 */
@Slf4j
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class UserServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(UserServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("用户服务模块启动完成!");
    }
}
