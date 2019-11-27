package com.bugjc.ea.opensdk.http.core.component.token;

import com.bugjc.ea.opensdk.http.core.exception.HttpSecurityException;
import com.bugjc.ea.opensdk.http.service.HttpService;

/**
 * 平台认证服务配置
 * @author aoki
 * @date 2019/11/5
 * **/
public interface AuthConfig {

    /**
     * 设置 平台接口认证服务
     * @param httpService
     * @date 2019/11/5
     **/
    void setHttpService(HttpService httpService);

    /**
     * 获取 token
     * @return
     * @throws HttpSecurityException
     * @date 2019/11/5
     */
    String getToken() throws HttpSecurityException;
}
