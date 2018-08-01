package com.bugjc.ea.qrcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 程序入口
 * @author qingyang
 */
@ComponentScan(value = "com.bugjc.ea")
@EnableHystrix
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class CodeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeServerApplication.class, args);
    }
}
