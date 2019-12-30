package com.bugjc.ea.opensdk.aop;


import com.bugjc.ea.opensdk.aop.aspect.DefaultAspect;
import com.bugjc.ea.opensdk.aop.service.JobService;
import com.bugjc.ea.opensdk.aop.service.impl.JobServiceImpl;

class AopTest {
    public static void main(String[] args) {
        JobService jobService = (JobService) ProxyUtil.createProxy(new JobServiceImpl(),new DefaultAspect());
        jobService.create("创建数据转移任务");
    }
}