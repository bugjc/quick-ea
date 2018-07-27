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
 * zipkin test
 * @author qingyang
 */
@Slf4j
@RestController
public class ZipkinController {

    private final RestTemplate restTemplate;

    private final ZipkinService zipkinService;


    @Autowired
    public ZipkinController(RestTemplate restTemplate, ZipkinService zipkinService) {
        this.restTemplate = restTemplate;
        this.zipkinService = zipkinService;
    }

    @GetMapping(value = "service-a-message")
    public String testaMessage() {
        return "service-a-message";
    }

    @GetMapping(value = "/testa/{userId}")
    public String testa(@PathVariable String userId) {
        log.info("param:"+userId);
        zipkinService.orderStep1("U20");
        return restTemplate.getForObject("http://api-v2/service-b-message", String.class);
    }




}
