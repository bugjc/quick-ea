package com.bugjc.ea.member.web;

import com.bugjc.ea.member.service.ZipkinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * zipkin test
 * @author qingyang
 */
@Slf4j
@RequestMapping("zipkin")
@RestController
public class ZipkinController {

    private final RestTemplate restTemplate;

    private final ZipkinService zipkinService;


    @Autowired
    public ZipkinController(RestTemplate restTemplate, ZipkinService zipkinService) {
        this.restTemplate = restTemplate;
        this.zipkinService = zipkinService;
    }

    @GetMapping(value = "message")
    public String message() {
        return "会员服务 zipkin 测试模块应答消息";
    }

    @GetMapping(value = "{userId}")
    public String memberServerTest(@PathVariable String userId) {
        log.info("param:"+userId);
        zipkinService.stepOne(userId);
        return restTemplate.getForObject("http://qrcode-server/zipkin/message", String.class);
    }

    @GetMapping(value = "/test/{userId}")
    public String stepOne(@PathVariable String userId){
        zipkinService.stepOne(userId);
        return userId;
    }


}
