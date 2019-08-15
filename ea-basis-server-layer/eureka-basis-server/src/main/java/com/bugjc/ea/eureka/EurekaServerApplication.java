package com.bugjc.ea.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 程序入口
 * @author qingyang
 */
@Slf4j
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication implements CommandLineRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Eureka 基础服务模块启动完成!");
    }
}
