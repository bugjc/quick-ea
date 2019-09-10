package com.bugjc.ea.auth.service;

import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.Map;

/**
 * @author aoki
 */
public interface ZuulRouteService {

    /**
     * 从db中加载路由
     */
    Map<String, ZuulProperties.ZuulRoute> locateRoutesFromDataBase();

}
