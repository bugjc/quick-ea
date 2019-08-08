package com.bugjc.ea.gateway.web;

import com.alibaba.fastjson.JSON;
import com.bugjc.ea.gateway.service.RefreshRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author aoki
 */
@Slf4j
@RestController
public class DemoController {

    @Resource
    private RefreshRouteService refreshRouteService;
    @Resource
    private ZuulHandlerMapping zuulHandlerMapping;

    @RequestMapping("/refreshRoute")
    public String refreshRoute(){
        refreshRouteService.refreshRoute();
        return "refreshRoute";
    }

    @RequestMapping("/watchNowRoute")
    public String watchNowRoute(){
        //可以用debug模式看里面具体是什么
        Map<String, Object> handlerMap = zuulHandlerMapping.getHandlerMap();
        log.info(JSON.toJSONString(handlerMap));
        return "watchNowRoute";
    }



}
