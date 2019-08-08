package com.bugjc.ea.gateway.service;

import com.bugjc.ea.gateway.model.CustomZuulRoute;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.Map;

/**
 * @author aoki
 */
public interface ZuulRouteService {

    /**
     * 检查路由方式
     *
     * @param appId
     * @return true:物理路由方式，false:eureka服务方式
     */
    boolean checkRouteMode(String appId, String path, boolean isDebug);

    /**
     * 获取当前请求的路由配置
     *
     * @param appId
     * @param path
     * @return
     */
    CustomZuulRoute findByAppIdAndPath(String appId, String path, boolean isDebug);

    /**
     * 从db中加载路由
     */
    Map<String, ZuulProperties.ZuulRoute> locateRoutesFromDataBase();

}
