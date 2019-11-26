package com.bugjc.ea.gateway.zuul.config;

import com.bugjc.ea.gateway.zuul.service.ZuulRouteService;
import com.bugjc.ea.gateway.zuul.core.zuul.CustomRouteLocator;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * @author aoki
 */
@Configuration
public class CustomZuulConfig {

    @Resource
    ZuulProperties zuulProperties;
    @Resource
    ServerProperties server;
    @Resource
    ZuulRouteService zuulRouteService;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Bean
    public CustomRouteLocator routeLocator() {
        return new CustomRouteLocator(
                this.server.getServlet().getContextPath(),
                this.zuulProperties,
                this.zuulRouteService,
                this.stringRedisTemplate);
    }

}
