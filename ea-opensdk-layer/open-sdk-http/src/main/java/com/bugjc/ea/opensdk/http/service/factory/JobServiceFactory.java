package com.bugjc.ea.opensdk.http.service.factory;

import com.bugjc.ea.opensdk.http.service.HttpService;
import com.bugjc.ea.opensdk.http.service.JobService;
import com.bugjc.ea.opensdk.http.service.impl.JobServiceImpl;
import com.google.inject.Inject;
import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * http service 工厂类
 * @author aoki
 * @date 2019/12/10
 * **/
@Slf4j
public class JobServiceFactory implements Provider<JobService> {

    @Inject
    private HttpService httpService;

    @Override
    public JobService get() {
        log.info("构建 AuthServiceImpl 依赖项 HttpService = {}", httpService);
        //return (HttpService) ProxyUtil.createProxy(new HttpServiceImpl(authService, jobService), HttpCallAspect.class);
        return new JobServiceImpl(httpService);
    }
}
