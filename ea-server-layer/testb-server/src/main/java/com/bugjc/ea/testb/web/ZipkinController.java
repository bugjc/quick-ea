package com.bugjc.ea.testb.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * zipkin test
 * @author qingyang
 */
@Slf4j
@RestController
public class ZipkinController {

    private final RestTemplate restTemplate;

    @Autowired
    public ZipkinController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "service-b-message")
    public String testbMessage() {
        log.info("调用成功！");
        return "service-b-message";
    }

    @GetMapping(value = "/testb/{userId}")
    public String testb(@PathVariable String userId) {
        log.info("param:"+userId);
        return restTemplate.getForObject("http://api-v1/service-a-message", String.class);
    }

}
