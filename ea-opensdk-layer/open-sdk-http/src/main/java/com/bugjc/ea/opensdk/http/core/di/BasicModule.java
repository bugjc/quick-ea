package com.bugjc.ea.opensdk.http.core.di;

import com.bugjc.ea.opensdk.http.service.HttpService;
import com.bugjc.ea.opensdk.http.service.impl.HttpServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * 定义依赖绑定的基本单元
 * @author aoki
 * @date 2019/12/9
 * **/
public class BasicModule extends AbstractModule {

    @Override
    protected void configure() {
        // 表明：当需要 HttpService 这个变量时，注入 单例的 HttpServiceImpl 实例作为依赖。
        this.bind(HttpService.class).to(HttpServiceImpl.class).in(Scopes.SINGLETON);
    }
}
