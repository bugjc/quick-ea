package com.bugjc.ea.qrcode.web;

import com.bugjc.ea.qrcode.core.api.MemberApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.UUID;

/**
 * zipkin test
 * @author qingyang
 */
@Slf4j
@RequestMapping("zipkin")
@RestController
public class ZipkinController {

    private final RestTemplate restTemplate;

    private final MemberApi memberApi;

    @Autowired
    public ZipkinController(RestTemplate restTemplate, MemberApi memberApi) {
        this.restTemplate = restTemplate;
        this.memberApi = memberApi;
    }

    @GetMapping(value = "message")
    public String message() {
        log.info("调用成功！");
        memberApi.stepOne(UUID.randomUUID().toString());
        return "二维码服务 zipkin 模块应答消息";
    }

    @GetMapping(value = "{userId}")
    public String qrcodeServerTest(@PathVariable String userId) {
        log.info("param:"+userId);
        return restTemplate.getForObject("http://member-server/zipkin/message", String.class);
    }

}
