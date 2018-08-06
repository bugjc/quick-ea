package com.bugjc.ea.qrcode.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RoundRobinRule;
import feign.Contract;
import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 客户端 feign 配置
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

    /**
     *
     *
     * 显式的指定使用轮询算法
     * @return
     */
    @Bean
    public IRule myRule() {
        //指定随机算法路由
        return new RandomRule();
    }

}
