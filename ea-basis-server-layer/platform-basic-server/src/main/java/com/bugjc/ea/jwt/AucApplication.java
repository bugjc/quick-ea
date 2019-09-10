package com.bugjc.ea.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 鉴权中心程序入口
 * @author aoki
 */
@Slf4j
@EnableEurekaClient
@SpringBootApplication
public class AucApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AucApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("AUT 基础服务模块启动完成!");
    }
}
