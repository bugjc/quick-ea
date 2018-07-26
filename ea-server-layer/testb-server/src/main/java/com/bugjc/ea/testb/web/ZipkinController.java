package com.bugjc.ea.testb.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: qingyang
 * @Date: 2018/7/25 17:27
 * @Description:
 */
@Slf4j
@RestController
public class ZipkinController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "service-b-message", method = RequestMethod.GET)
    public String serviceBMessage() {
        log.info("调用成功！");
        return "service-b-message";
    }

    @RequestMapping(value = "service-b", method = RequestMethod.GET)
    public String serviceB() {
        return restTemplate.getForObject("http://api-v1/service-a-message", String.class);
    }


}
