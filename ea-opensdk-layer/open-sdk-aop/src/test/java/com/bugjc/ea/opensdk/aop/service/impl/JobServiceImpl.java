package com.bugjc.ea.opensdk.aop.service.impl;

import com.bugjc.ea.opensdk.aop.service.JobService;

public class JobServiceImpl implements JobService {
    @Override
    public void create(String jobName) {
        System.out.println("创建任务："+jobName);
    }
}
