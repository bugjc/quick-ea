package com.bugjc.ea.gateway.service.impl;

import com.bugjc.ea.gateway.service.RefreshRouteService;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author aoki
 */
@Service
public class RefreshRouteServiceImpl implements RefreshRouteService {

    @Resource
    private ApplicationEventPublisher publisher;

    @Resource
    private RouteLocator routeLocator;

    @Override
    public void refreshRoute() {
        RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
        publisher.publishEvent(routesRefreshedEvent);
    }

}
