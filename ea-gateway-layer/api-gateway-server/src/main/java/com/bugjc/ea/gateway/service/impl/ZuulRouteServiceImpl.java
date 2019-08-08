package com.bugjc.ea.gateway.service.impl;

import com.bugjc.ea.gateway.service.ZuulRouteService;
import com.bugjc.ea.gateway.mapper.AppRouteMapper;
import com.bugjc.ea.gateway.mapper.ZuulRouteMapper;
import com.bugjc.ea.gateway.model.AppRoute;
import com.bugjc.ea.gateway.model.CustomZuulRoute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aoki
 */
@Slf4j
@Service
public class ZuulRouteServiceImpl implements ZuulRouteService {

    @Resource
    private ZuulRouteMapper zuulRouteMapper;
    @Resource
    private AppRouteMapper appRouteMapper;


    @Override
    public boolean checkRouteMode(String appId, String path, boolean isDebug) {
        if (isDebug) {
            return true;
        }
        List<AppRoute> appRoutes = appRouteMapper.selectByAppId(appId);
        if (appRoutes == null || appRoutes.isEmpty()) {
            log.error("APP{}未配置应用路由。", appId);
            return false;
        }

        for (AppRoute appRoute : appRoutes) {
            CustomZuulRoute zuulRoute = zuulRouteMapper.selectById(appRoute.getRouteId());
            String pathPrefix = zuulRoute.getPath().split("/")[1];
            if (!path.startsWith("/".concat(pathPrefix))) {
                continue;
            }

            if (zuulRoute.getServiceId() == null && zuulRoute.getUrl() != null && zuulRoute.getRibbonUrl() != null) {
                //配置了物理地址和负载地址的进入均衡过滤器
                return true;
            }

            if (appRoute.getIsDebug() != null && appRoute.getIsDebug()) {
                //服务注册方式已开启debug模式，接下来的地址会被路由到物理地址中
                return true;
            }
        }

        return false;
    }

    @Override
    public CustomZuulRoute findByAppIdAndPath(String appId, String path, boolean isDebug) {
        List<AppRoute> appRoutes = appRouteMapper.selectByAppId(appId);
        if (appRoutes == null || appRoutes.isEmpty()) {
            log.error("APP{}未配置应用路由。", appId);
            return null;
        }

        for (AppRoute appRoute : appRoutes) {
            CustomZuulRoute zuulRoute = zuulRouteMapper.selectById(appRoute.getRouteId());
            String pathPrefix = zuulRoute.getPath().split("/")[1];
            if (path.startsWith("/".concat(pathPrefix))) {
                return zuulRoute;
            }
        }
        return null;
    }

    @Override
    public Map<String, ZuulProperties.ZuulRoute> locateRoutesFromDataBase() {
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();
        List<CustomZuulRoute> results = zuulRouteMapper.selectAll();
        for (CustomZuulRoute result : results) {
            ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
            try {
                org.springframework.beans.BeanUtils.copyProperties(result,zuulRoute);
            } catch (Exception e) {
                log.error("从MYSQL DB中加载路由失败",e);
            }
            routes.put(zuulRoute.getPath(),zuulRoute);
        }
        return routes;
    }

}
