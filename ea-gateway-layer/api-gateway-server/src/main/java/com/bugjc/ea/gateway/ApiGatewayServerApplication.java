package com.bugjc.ea.gateway;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.bugjc.ea.gateway.core.filter.CustomFilterProcessor;
import com.netflix.zuul.FilterProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 程序入口
 * @author qingyang
 */
@EnableMethodCache(basePackages = "com.bugjc.ea.gateway")
@EnableCreateCacheAnnotation
@EnableEurekaClient
@EnableZuulProxy
@SpringBootApplication
public class ApiGatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayServerApplication.class, args);
        //自定义过滤处理器
        FilterProcessor.setProcessor(new CustomFilterProcessor());
    }

}
