package com.bugjc.ea.qrcode.web;

import com.alibaba.fastjson.JSON;
import com.bugjc.ea.qrcode.core.dto.Result;
import com.bugjc.ea.qrcode.core.dto.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author qingyang
 * @date 2018/8/2 15:48
 */
@Slf4j
@RestController
public class LoadTestingController {

    @Resource
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("getMemberServiceInstance")
    public Result getServiceInstance(){
        //轮询访问策略
        ServiceInstance serviceInstance = loadBalancerClient.choose("member-server");
        if (serviceInstance == null){
            return ResultGenerator.genFailResult("依赖服务未启动");
        }
        log.info(serviceInstance.getScheme());
        log.info(JSON.toJSONString(serviceInstance.getMetadata()));
        return ResultGenerator.genSuccessResult(serviceInstance);
    }


}
