package com.bugjc.ea.gateway.zuul.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.Tester;
import com.bugjc.ea.gateway.zuul.core.enums.business.AppType;
import com.bugjc.ea.gateway.zuul.mapper.*;
import com.bugjc.ea.gateway.zuul.model.*;
import com.bugjc.ea.gateway.zuul.util.CreateSecurityKey;
import com.bugjc.ea.gateway.zuul.util.IdWorker;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.dao.DuplicateKeyException;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Data
    private static class AppConfig implements Serializable {
        //应用名称(必填)
        private String appName;
        //应用描述 (必填)
        private String appDesc;
        //应用编码，4位，不重复 (必填)
        private String appCode;
        //应用类型：1 - 业务,，2 - 平台(必填,暂未用到)
        private int appType;
        //路由匹配地址 (必填)
        private String path;
        //跳转的物理地址
        private String url;
        //配置了物理地址的使用
        private String ribbonUrl;
        //是否剥离前缀
        private boolean stripPrefix = true;
        //是否启用
        private boolean enabled = true;
    }

    /**
     * 应用配置列表
     */
    private static List<AppConfig> appConfigList = new ArrayList<>();;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        //添加“平台任务服务”配置
        AppConfig authAppConfig = new AppConfig();
        authAppConfig.setAppCode("AUTH");
        authAppConfig.setAppName("auth-basic-server");
        authAppConfig.setAppType(AppType.Business.getType());
        authAppConfig.setAppDesc("平台认证服务");
        authAppConfig.setPath("/auth/**");
        appConfigList.add(authAppConfig);

        //添加“任务调度服务”配置
        AppConfig jobAppConfig = new AppConfig();
        jobAppConfig.setAppCode("JOB0");
        jobAppConfig.setAppName("job-server");
        jobAppConfig.setAppType(AppType.Business.getType());
        jobAppConfig.setAppDesc("任务调度服务");
        jobAppConfig.setPath("/job/**");
        jobAppConfig.setStripPrefix(false);
        appConfigList.add(jobAppConfig);

        //添加“脚手架服务项目”配置
        AppConfig templateAppConfig = new AppConfig();
        templateAppConfig.setAppCode("TEMP");
        templateAppConfig.setAppName("template-server");
        templateAppConfig.setAppType(AppType.Business.getType());
        templateAppConfig.setAppDesc("脚手架服务项目");
        templateAppConfig.setPath("/template/**");
        appConfigList.add(templateAppConfig);

    }


    /**
     * 创建一个业务应用
     */
    @Test
    public void testCreateBusinessApp(){
        for (AppConfig appConfig : appConfigList) {

            //获取配置
            String appDesc = appConfig.getAppDesc();
            String appCode = appConfig.getAppCode();
            String path = appConfig.getPath();
            String appName = appConfig.getAppName();
            String url = appConfig.getUrl();
            String ribbonUrl = appConfig.getRibbonUrl();
            boolean enabled = appConfig.isEnabled();
            boolean stripPrefix = appConfig.isStripPrefix();
            Date date = new Date();

            try {
                //1.创建业务路由配置

                CustomZuulRoute customZuulRoute = new CustomZuulRoute();
                customZuulRoute.setId(appName);
                customZuulRoute.setPath(path);
                customZuulRoute.setServiceId(appName);
                customZuulRoute.setUrl(url);
                customZuulRoute.setRibbonUrl(ribbonUrl);
                customZuulRoute.setRetryable(false);
                customZuulRoute.setStripPrefix(stripPrefix);
                customZuulRoute.setEnabled(enabled);
                customZuulRoute.setDescription(appDesc);
                zuulRouteMapper.insert(customZuulRoute);

                //2.创建APP
                String appId = appCode.concat(IdWorker.getNextId());
                App app = new App();
                app.setAppSecret(RandomUtil.randomNumbers(24));
                app.setCreateTime(date);
                app.setDescription(appDesc);
                app.setType(AppType.Business.getType());
                app.setEnabled(enabled);
                app.setId(appId);
                BeanUtil.copyProperties(CreateSecurityKey.getKeyPair(),app);
                log.info("APP：{}", JSON.toJSONString(app));
                appMapper.insert(app);

                //3.创建应用路由类
                AppRoute appRoute = new AppRoute();
                appRoute.setId(appCode.concat(IdWorker.getNextId()));
                appRoute.setAppId(appId);
                appRoute.setRouteId(customZuulRoute.getId());
                appRoute.setIsDebug(false);
                appRoute.setEnabled(enabled);
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
                appVersionMap.setDescription(appDesc);
                appVersionMap.setCreateTime(date);
                appVersionMapMapper.insert(appVersionMap);

                //5.创建安全配置
                AppSecurityConfig appSecurityConfig = new AppSecurityConfig();
                appSecurityConfig.setId(appCode.concat(IdWorker.getNextId()));
                appSecurityConfig.setAppId(app.getId());
                appSecurityConfig.setPath(path);
                appSecurityConfig.setVerifySignature(true);
                appSecurityConfig.setVerifyToken(true);
                appSecurityConfig.setEnabled(enabled);
                appSecurityConfig.setDescription(appDesc);
                appSecurityConfig.setCreateTime(date);
                appSecurityConfigMapper.insert(appSecurityConfig);
                log.info("创建成功");
            }catch (DuplicateKeyException ex){
                log.info("忽略重复创建");
            }
        }
    }

}
