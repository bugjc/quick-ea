package com.bugjc.ea.testb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 程序入口
 * @author qingyang
 */
@EnableEurekaClient
@SpringBootApplication
public class Test2Application {

    public static void main(String[] args) {
        SpringApplication.run(Test2Application.class, args);
    }
}
