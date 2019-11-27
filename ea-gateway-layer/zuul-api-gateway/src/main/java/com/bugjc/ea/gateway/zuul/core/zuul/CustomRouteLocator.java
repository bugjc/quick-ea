package com.bugjc.ea.gateway.zuul.core.zuul;


import com.alibaba.fastjson.JSON;
import com.bugjc.ea.gateway.zuul.service.ZuulRouteService;
import com.bugjc.ea.opensdk.http.core.component.eureka.EurekaConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author aoki
 */
@Slf4j
public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator{

    private ZuulRouteService zuulRouteService;
    private ZuulProperties properties;
    private StringRedisTemplate stringRedisTemplate;

    public CustomRouteLocator(String servletPath,
                              ZuulProperties properties,
                              ZuulRouteService zuulRouteService,
                              StringRedisTemplate stringRedisTemplate) {
        super(servletPath, properties);
        this.properties = properties;
        this.zuulRouteService = zuulRouteService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulRoute> locateRoutes() {
        log.info("加载|刷新路由配置");

        //从application.properties中加载路由信息
        LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap<>(super.locateRoutes());
        //从db中加载路由信息
        Map<String, ZuulProperties.ZuulRoute> routeMap = zuulRouteService.locateRoutesFromDataBase();
        routesMap.putAll(routeMap);

        //优化一下配置
        LinkedHashMap<String, ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            // Prepend with slash if not already present.
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }

        //将路由信息存储到redis,为 sdk-http 项目发起调用时判断接口对应 eureka服务名称或物理地址,是 eureka 服务名称则会查询服务物理地址。
        stringRedisTemplate.opsForValue().set(EurekaConstants.ZUUL_ROUTE_CONFIG_INFO, JSON.toJSONString(new ArrayList<>(routesMap.values())));

        //清空数据
        routesMap.clear();
        return values;
    }
}