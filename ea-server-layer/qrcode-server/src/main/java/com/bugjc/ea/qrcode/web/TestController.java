package com.bugjc.ea.qrcode.web;

import com.bugjc.ea.qrcode.core.api.TestFeignClient;
import com.bugjc.ea.qrcode.service.impl.TestRemoteServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * zipkin test
 * hystrix test
 * @author qingyang
 */
@Slf4j
@RequestMapping("test")
@RestController
public class TestController {

    @Resource
    private TestRemoteServiceImpl testRemoteService;

    @GetMapping(value = "message")
    public String message() {
        return "二维码服务 zipkin 模块应答消息";
    }

    @GetMapping(value = "/hystrix/{userId}")
    public String testHystrix(@PathVariable String userId){
        return testRemoteService.testHystrix(userId);
    }

    @GetMapping(value = "/zipkin/{userId}")
    public String testZipkin(@PathVariable String userId){
        log.info(userId);
        return null;//restTemplate.getForObject("http://member-server/test/message", String.class);
    }

}
