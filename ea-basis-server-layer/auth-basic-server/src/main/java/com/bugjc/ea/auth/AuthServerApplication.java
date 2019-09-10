package com.bugjc.ea.auth;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 程序入口
 * @author qingyang
 */
@Slf4j
@EnableMethodCache(basePackages = "com.bugjc.ea.auth")
@EnableCreateCacheAnnotation
@EnableEurekaClient
@SpringBootApplication
public class AuthServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("平台认证服务模块启动完成!");
    }
}
