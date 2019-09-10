package com.bugjc.ea.auth.config;

import com.bugjc.ea.auth.service.ZuulRouteService;
import com.bugjc.ea.auth.core.zuul.CustomRouteLocator;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public CustomRouteLocator routeLocator() {
        CustomRouteLocator routeLocator = new CustomRouteLocator(this.server.getServlet().getContextPath(), this.zuulProperties);
        routeLocator.setZuulRouteService(zuulRouteService);
        return routeLocator;
    }

}
