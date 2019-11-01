package com.bugjc.ea.gateway.zuul.service;

import com.bugjc.ea.gateway.zuul.model.App;

/**
 * @author aoki
 */
public interface AppService {

    /**
     * 检查路由权限
     * @param appId
     * @param path
     * @return
     */
    boolean checkRoutePermission(String appId,String path);

    /**
     * 根据应用id 获取应用信息
     * @param appId
     * @return
     */
    App findByAppId(String appId);

    /**
     * 配置app
     * @param app
     */
    void add(App app);
}
