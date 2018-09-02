package com.bugjc.ea.qrcode;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 程序入口
 * @author aoki
 */
@ComponentScan(basePackages = "com.bugjc.ea")
@EnableHystrix
@EnableFeignClients
@EnableEurekaClient
@EnableMethodCache(basePackages = "com.bugjc.ea")
@EnableCreateCacheAnnotation
@SpringBootApplication
public class QrCodeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrCodeServerApplication.class, args);
    }
}
