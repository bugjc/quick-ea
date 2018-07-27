package com.bugjc.ea.testa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 程序入口
 * @author qingyang
 */
@EnableEurekaClient
@SpringBootApplication
public class Test1Application {

    public static void main(String[] args) {
        SpringApplication.run(Test1Application.class, args);
    }
}
