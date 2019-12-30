package com.bugjc.ea.opensdk.aop.service;

import com.bugjc.ea.opensdk.aop.Aspect;

@Aspect
public interface JobService {

    /**
     * 创建任务
     * @param jobName
     */
    void create(String jobName);
}
