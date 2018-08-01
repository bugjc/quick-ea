package com.bugjc.ea.member.web;

import com.bugjc.ea.member.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * zipkin test
 * hystrix testø
 * @author qingyang
 */
@Slf4j
@RequestMapping("test")
@RestController
public class TestController {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private TestService zipkinService;


    @GetMapping(value = "message")
    public String message() {
        return "会员服务 zipkin 测试模块应答消息";
    }

    @GetMapping(value = "/hystrix/{userId}")
    public String testHystrix(@PathVariable String userId){
        return zipkinService.testHystrix(userId);
    }

    @GetMapping(value = "/zipkin/{userId}")
    public String testZipkin(@PathVariable String userId){
        zipkinService.testZipkin(userId);
        return restTemplate.getForObject("http://qrcode-server/test/message", String.class);
    }


}
