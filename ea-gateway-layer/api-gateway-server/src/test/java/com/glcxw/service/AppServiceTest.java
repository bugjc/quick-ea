package com.glcxw.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.gateway.mapper.*;
import com.bugjc.ea.gateway.model.*;
import com.bugjc.ea.gateway.service.AppService;
import com.glcxw.Tester;
import com.glcxw.util.CreateSecurityKey;
import com.glcxw.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
public class AppServiceTest extends Tester {

    @Resource
    private AppMapper appMapper;
    @Resource
    private ZuulRouteMapper zuulRouteMapper;
    @Resource
    private AppRouteMapper appRouteMapper;
    @Resource
    private AppVersionMapMapper appVersionMapMapper;
    @Resource
    private AppSecurityConfigMapper appSecurityConfigMapper;

    /**
     * 创建一个业务应用
     */
    @Test
    public void testCreateBusinessApp(){
        String desc = "授权认证业务";
        String appCode = "DDDD";
        Date date = new Date();
        //1.创建APP
        App app = new App();
        app.setCreateTime(date);
        app.setDescription(desc);
        app.setType(1);
        app.setEnabled(true);
        app.setAppId(appCode.concat(IdWorker.getNextId()));
        BeanUtil.copyProperties(CreateSecurityKey.getKeyPair(),app);
        log.info("APP：{}",JSON.toJSONString(app));
        appMapper.insert(app);

        //2.创建业务路由配置
        String path = "/jwt/**";
        String applicationName = "jwt-server";
        CustomZuulRoute customZuulRoute = new CustomZuulRoute();
        customZuulRoute.setId(applicationName);
        customZuulRoute.setPath(path);
        customZuulRoute.setServiceId(applicationName);
        customZuulRoute.setUrl(null);
        customZuulRoute.setRibbonUrl(null);
        customZuulRoute.setRetryable(false);
        customZuulRoute.setStripPrefix(true);
        customZuulRoute.setEnabled(true);
        customZuulRoute.setDescription(desc);
        zuulRouteMapper.insert(customZuulRoute);

        //3.创建应用路由类
        AppRoute appRoute = new AppRoute();
        appRoute.setAppId(app.getAppId());
        appRoute.setRouteId(customZuulRoute.getId());
        appRoute.setIsDebug(false);
        appRoute.setEnabled(true);
        appRoute.setCreateTime(date);
        appRouteMapper.insert(appRoute);

        //4.创建版本映射表
        String defaultVersion = "1.0";
        AppVersionMap appVersionMap = new AppVersionMap();
        appVersionMap.setAppId(app.getAppId());
        appVersionMap.setVersionNo(defaultVersion);
        appVersionMap.setPath("/");
        appVersionMap.setMapPath("/");
        appVersionMap.setDescription(desc);
        appVersionMap.setCreateTime(date);
        appVersionMapMapper.insert(appVersionMap);

        //5.创建安全配置
        AppSecurityConfig appSecurityConfig = new AppSecurityConfig();
        appSecurityConfig.setAppId(app.getAppId());
        appSecurityConfig.setPath(path);
        appSecurityConfig.setVerifySignature(true);
        appSecurityConfig.setVerifyToken(true);
        appSecurityConfig.setEnabled(true);
        appSecurityConfig.setDescription(desc);
        appSecurityConfig.setCreateTime(date);
        appSecurityConfigMapper.insert(appSecurityConfig);
    }

}
