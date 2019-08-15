package com.bugjc.ea.qrcode;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
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
@Slf4j
@ComponentScan(basePackages = "com.bugjc.ea")
@EnableHystrix
@EnableFeignClients
@EnableEurekaClient
@EnableMethodCache(basePackages = "com.bugjc.ea")
@EnableCreateCacheAnnotation
@SpringBootApplication
public class QrCodeServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(QrCodeServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("消费二维码服务模块启动完成!");
    }
}
