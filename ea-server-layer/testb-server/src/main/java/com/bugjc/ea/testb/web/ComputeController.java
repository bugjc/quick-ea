package com.bugjc.ea.testb.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: qingyang
 * @Date: 2018/7/22 15:04
 * @Description:
 */
@Slf4j
@RestController
public class ComputeController {


    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping(value = "/add/{a}/{b}",method = RequestMethod.GET)
    public String add(@PathVariable Integer a, @PathVariable Integer b) {


        discoveryClient.getServices().forEach(id -> {
            discoveryClient.getInstances(id).forEach(instance -> {
                log.info("/hello, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());
            });
        });

        return (a+b)+"";
    }

}
