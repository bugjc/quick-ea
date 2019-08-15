package com.bugjc.ea.member;

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
public class MemberServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MemberServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("会员服务模块启动完成!");
    }
}
