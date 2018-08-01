package com.bugjc.ea.qrcode.service.impl;

import com.bugjc.ea.qrcode.core.api.TestFeignClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 容错处理
 * @author qingyang
 * @date 2018/7/31 10:51
 */
@Slf4j
@Service
public class TestRemoteServiceImpl {

    @Resource
    private TestFeignClient testFeignClient;

    @HystrixCommand(fallbackMethod = "testHystrixFallback")
    public String testHystrix(String userId){
        return testFeignClient.testHystrix(userId);
    }

    public String testHystrixFallback(String userId,Throwable e){
        log.error(e.getMessage());
        log.info("熔断处理");
        return userId;
    }
}
