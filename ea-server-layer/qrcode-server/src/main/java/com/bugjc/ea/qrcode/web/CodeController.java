package com.bugjc.ea.qrcode.web;

import com.bugjc.ea.qrcode.service.impl.TestRemoteServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 二维码服务
 * @author qingyang
 */
@Slf4j
@RestController
public class CodeController {

    @Resource
    private TestRemoteServiceImpl testRemoteService;

    @GetMapping(value = "/qrcode/get")
    public String testHystrix(@PathVariable String userId){
        return testRemoteService.testHystrix(userId);
    }
}
