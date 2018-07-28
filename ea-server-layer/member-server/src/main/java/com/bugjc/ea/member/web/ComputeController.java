package com.bugjc.ea.member.web;

import com.bugjc.ea.member.config.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 * @author qingyang
 */
@Slf4j
@RestController
public class ComputeController {


    private final DiscoveryClient discoveryClient;
    private final ApplicationConfig applicationConfig;

    @Autowired
    public ComputeController(DiscoveryClient discoveryClient, ApplicationConfig applicationConfig) {
        this.discoveryClient = discoveryClient;
        this.applicationConfig = applicationConfig;
    }

    @RequestMapping(value = "/add/{a}/{b}",method = RequestMethod.GET)
    public String add(@PathVariable Integer a, @PathVariable Integer b) {


        discoveryClient.getServices().forEach(id -> {
            discoveryClient.getInstances(id).forEach(instance -> {
                log.info("/hello, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());
            });
        });

        return (a+b)+" "+ applicationConfig.applicationName;
    }

}
