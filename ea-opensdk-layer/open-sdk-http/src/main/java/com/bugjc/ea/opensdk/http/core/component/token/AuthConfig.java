package com.bugjc.ea.opensdk.http.core.component.token;

import com.bugjc.ea.opensdk.http.core.exception.HttpSecurityException;

/**
 * 平台认证服务配置
 * @author aoki
 * @date 2019/11/5
 * **/
public interface AuthConfig {

    /**
     * 获取 token
     * @return
     * @throws HttpSecurityException
     * @date 2019/11/5
     */
    String getToken() throws HttpSecurityException;
}
