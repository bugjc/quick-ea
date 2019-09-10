package com.glcxw.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.auth.mapper.*;
import com.bugjc.ea.auth.model.*;
import com.glcxw.Tester;
import com.glcxw.util.CreateSecurityKey;
import com.glcxw.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

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
        String desc = "用户服务";
        String appCode = "USER";
        Date date = new Date();
        //1.创建APP
        App app = new App();
        app.setAppSecret(RandomUtil.randomNumbers(24));
        app.setCreateTime(date);
        app.setDescription(desc);
        app.setType(1);
        app.setEnabled(true);
        app.setId(appCode.concat(IdWorker.getNextId()));
        BeanUtil.copyProperties(CreateSecurityKey.getKeyPair(),app);
        log.info("APP：{}",JSON.toJSONString(app));
        appMapper.insert(app);

        //2.创建业务路由配置
        String path = "/user/**";
        String applicationName = "user-server";
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
        appRoute.setId(appCode.concat(IdWorker.getNextId()));
        appRoute.setAppId(app.getId());
        appRoute.setRouteId(customZuulRoute.getId());
        appRoute.setIsDebug(false);
        appRoute.setEnabled(true);
        appRoute.setCreateTime(date);
        appRouteMapper.insert(appRoute);

        //4.创建版本映射表
        String defaultVersion = "1.0";
        AppVersionMap appVersionMap = new AppVersionMap();
        appVersionMap.setId(appCode.concat(IdWorker.getNextId()));
        appVersionMap.setAppId(app.getId());
        appVersionMap.setVersionNo(defaultVersion);
        appVersionMap.setPath("/");
        appVersionMap.setMapPath("/");
        appVersionMap.setDescription(desc);
        appVersionMap.setCreateTime(date);
        appVersionMapMapper.insert(appVersionMap);

        //5.创建安全配置
        AppSecurityConfig appSecurityConfig = new AppSecurityConfig();
        appSecurityConfig.setId(appCode.concat(IdWorker.getNextId()));
        appSecurityConfig.setAppId(app.getId());
        appSecurityConfig.setPath(path);
        appSecurityConfig.setVerifySignature(true);
        appSecurityConfig.setVerifyToken(true);
        appSecurityConfig.setEnabled(true);
        appSecurityConfig.setDescription(desc);
        appSecurityConfig.setCreateTime(date);
        appSecurityConfigMapper.insert(appSecurityConfig);
    }

}
