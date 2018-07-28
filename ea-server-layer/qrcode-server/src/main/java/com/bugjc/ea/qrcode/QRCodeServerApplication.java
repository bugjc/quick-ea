package com.bugjc.ea.qrcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 程序入口
 * @author qingyang
 */
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class QRCodeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QRCodeServerApplication.class, args);
    }
}
