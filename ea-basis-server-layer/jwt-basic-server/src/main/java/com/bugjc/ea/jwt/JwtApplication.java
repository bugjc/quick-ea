package com.bugjc.ea.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 程序入口
 * @author aoki
 */
@Slf4j
@EnableEurekaClient
@SpringBootApplication
public class JwtApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JwtApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("JWT 基础服务模块启动完成!");
    }
}
