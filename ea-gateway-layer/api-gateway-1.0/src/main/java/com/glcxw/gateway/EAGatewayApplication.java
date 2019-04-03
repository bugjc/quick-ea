package com.glcxw.gateway;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.glcxw.gateway.core.filter.CustomFilterProcessor;
import com.netflix.zuul.FilterProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 程序入口
 * @author qingyang
 */
@EnableMethodCache(basePackages = "com.glcxw.gateway")
@EnableCreateCacheAnnotation
@EnableEurekaClient
@EnableZuulProxy
@SpringBootApplication
public class EAGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(EAGatewayApplication.class, args);
        //自定义过滤处理器
        FilterProcessor.setProcessor(new CustomFilterProcessor());
    }

}
