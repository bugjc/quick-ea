package com.bugjc.ea.qrcode.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.qrcode.config.ApplicationConfig;
import com.bugjc.ea.qrcode.core.dto.Result;
import com.bugjc.ea.qrcode.core.dto.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qingyang
 * @date 2018/9/5 23:20
 */
@Slf4j
@RequestMapping
@RestController
public class ServiceInstanceController {

    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private ApplicationConfig applicationConfig;

    /**
     * 获取实例所有服务列表
     * @return
     */
    @GetMapping(value = "getServiceInstance")
    public Result getServiceInstanceList(){

        ServiceInstance localServiceInstance = applicationConfig.getLocalServiceInstance();
        if (localServiceInstance == null){
            return ResultGenerator.genFailResult("获取本地服务实例失败");
        }
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(localServiceInstance));
        JSONObject instanceInfo = jsonObject.getJSONObject("instanceInfo");
        log.info("localInstanceId:{}",instanceInfo.getString("instanceId"));

        List<ServiceInstance> list = discoveryClient.getInstances(applicationConfig.getApplicationName());
        for (ServiceInstance serviceInstance : list) {
            jsonObject = JSON.parseObject(JSON.toJSONString(serviceInstance));
            instanceInfo = jsonObject.getJSONObject("instanceInfo");
            log.info("instanceId:{}",instanceInfo.getString("instanceId"));
        }
        return ResultGenerator.genSuccessResult(JSON.toJSONString(list));
    }


}
