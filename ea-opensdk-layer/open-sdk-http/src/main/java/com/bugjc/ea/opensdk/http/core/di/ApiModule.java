package com.bugjc.ea.opensdk.http.core.di;

import com.bugjc.ea.opensdk.http.core.component.eureka.EurekaConfig;
import com.bugjc.ea.opensdk.http.core.component.eureka.impl.EurekaDefaultConfigImpl;
import com.bugjc.ea.opensdk.http.core.component.token.AuthConfig;
import com.bugjc.ea.opensdk.http.core.component.token.enums.AuthTypeEnum;
import com.bugjc.ea.opensdk.http.core.component.token.impl.AuthDefaultConfigImpl;
import com.bugjc.ea.opensdk.http.core.component.token.impl.AuthRedisConfigImpl;
import com.bugjc.ea.opensdk.http.service.AuthService;
import com.bugjc.ea.opensdk.http.service.HttpService;
import com.bugjc.ea.opensdk.http.service.JobService;
import com.bugjc.ea.opensdk.http.service.impl.AuthServiceImpl;
import com.bugjc.ea.opensdk.http.service.impl.JobServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import lombok.extern.slf4j.Slf4j;

/**
 * 定义依赖绑定的基本单元
 * @author aoki
 * @date 2019/12/9
 * **/
@Slf4j
public class ApiModule extends AbstractModule {

    /**
     * HTTP 客户端
     */
    private HttpService httpService;

    public ApiModule(HttpService httpService) {
        this.httpService = httpService;
    }

    @Override
    protected void configure() {
        //绑定服务实例依赖。
        this.bind(HttpService.class).toInstance(this.httpService);
        this.bind(AuthService.class).toInstance(new AuthServiceImpl(this.httpService));
        this.bind(JobService.class).toInstance(new JobServiceImpl(this.httpService));

        //绑定内部注册中心调用实例依赖
        this.bind(EurekaConfig.class).toInstance(new EurekaDefaultConfigImpl(this.httpService));
        //动态参数 AuthConfig  多实例绑定
        MapBinder<Integer, AuthConfig> authBinder = MapBinder.newMapBinder(binder(), Integer.class, AuthConfig.class);
        authBinder.addBinding(AuthTypeEnum.Memory.getType()).toInstance(AuthDefaultConfigImpl.getInstance());
        authBinder.addBinding(AuthTypeEnum.Redis.getType()).toInstance(AuthRedisConfigImpl.getInstance());

    }
}
