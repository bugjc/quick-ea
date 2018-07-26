package com.bugjc.ea.testa.web;

import brave.Span;
import brave.Tracer;
import com.bugjc.ea.testa.service.ZipkinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    ZipkinService zipkinService;

    @Autowired
    Tracer tracer;

    @RequestMapping(value = "service-a-message", method = RequestMethod.GET)
    public String serviceAMessage() {
        return "service-a-message";
    }

    @GetMapping(value = "/testa/{userId}")
    public String serviceA(@PathVariable String userId) {
        log.info("param:"+userId);
        zipkinService.orderStep1("U20");
        return restTemplate.getForObject("http://api-v2/service-b-message", String.class);
    }




}
