package com.bugjc.ea.qrcode.config;

import feign.Contract;
import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author qingyang
 * @date 2018/7/30 11:12
 */
@Configuration
public class FeignClientConfig {


    /**
     * 使用feign的注解方式
     * @return
     */
    @Bean
    public Contract useFeignAnnotations() {
        return new Contract.Default();
    }

    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }
}
