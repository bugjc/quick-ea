package com.bugjc.ea.gateway.zuul;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.bugjc.ea.gateway.zuul.core.filter.CustomFilterProcessor;
import com.netflix.zuul.FilterProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 程序入口
 * @author qingyang
 */
@Slf4j
@EnableMethodCache(basePackages = "com.bugjc.ea.gateway")
@EnableCreateCacheAnnotation
@EnableEurekaClient
@EnableZuulProxy
@SpringBootApplication
public class ZuulApiGatewayApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApiGatewayApplication.class, args);
        //自定义过滤处理器
        FilterProcessor.setProcessor(new CustomFilterProcessor());
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("API 网关服务模块启动完成!");
    }
}
