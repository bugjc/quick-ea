//package com.glcxw.wechat.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//
///**
// * @Author yangqing
// * @Date 2019/7/24 15:54
// **/
//
//@Configuration
//@PropertySource("classpath:redis.properties")
//@Slf4j
//public class JedisConfig {
//
//    @Value("${spring.redis.host}")
//    private String host;
//
//    @Value("${spring.redis.port}")
//    private int port;
//
//    @Value("${spring.redis.timeout}")
//    private int timeout;
//
//    @Value("${spring.redis.password}")
//    private String password;
//
//    @Bean
//    public JedisPool redisPoolFactory()  throws Exception{
//        log.info("JedisPool注入成功！！");
//        log.info("redis地址：" + host + ":" + port);
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(maxIdle);
//        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
//        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
//        jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
//        // 是否启用pool的jmx管理功能, 默认true
//        jedisPoolConfig.setJmxEnabled(true);
//        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
//        return jedisPool;
//    }
//
//}
