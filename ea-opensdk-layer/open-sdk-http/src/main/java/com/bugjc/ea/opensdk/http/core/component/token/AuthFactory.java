package com.bugjc.ea.opensdk.http.core.component.token;

import com.bugjc.ea.opensdk.http.core.component.token.AuthConfig;
import com.google.inject.Inject;

import java.util.Map;

/**
 * 授权服务工厂
 * @author aoki
 * @date 2019/12/11
 * **/
public class AuthFactory {

    @Inject
    private Map<Integer, AuthConfig> authMap;

    /**
     * 获取 AuthConfig 实例
     * @param type
     * @return
     */
    public AuthConfig get(int type){
        return authMap.getOrDefault(type, null);
    }
}
