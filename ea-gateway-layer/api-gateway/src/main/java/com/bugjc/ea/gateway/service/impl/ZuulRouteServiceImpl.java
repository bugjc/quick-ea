package com.bugjc.ea.gateway.service.impl;

import com.bugjc.ea.gateway.mapper.ZuulRouteMapper;
import com.bugjc.ea.gateway.model.CustomZuulRoute;
import com.bugjc.ea.gateway.service.ZuulRouteService;
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
