package com.bugjc.ea.opensdk.http.service.factory;

import com.bugjc.ea.opensdk.http.service.AuthService;
import com.bugjc.ea.opensdk.http.service.HttpService;
import com.bugjc.ea.opensdk.http.service.impl.AuthServiceImpl;
import com.google.inject.Inject;
import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * http service 工厂类
 * @author aoki
 * @date 2019/12/10
 * **/
@Slf4j
public class AuthServiceFactory implements Provider<AuthService> {

    @Inject
    private HttpService httpService;

    @Override
    public AuthService get() {
        log.info("构建 AuthServiceImpl 依赖项 HttpService = {}", httpService);
        //return (HttpService) ProxyUtil.createProxy(new HttpServiceImpl(authService, jobService), HttpCallAspect.class);
        return new AuthServiceImpl(httpService);
    }
}
