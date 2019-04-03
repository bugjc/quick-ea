package com.glcxw.gateway.service;

import com.glcxw.gateway.model.App;

/**
 * @author aoki
 */
public interface AppService {

    /**
     * 根据应用id 获取应用信息
     * @param appId
     * @return
     */
    App findByAppId(String appId);
}
