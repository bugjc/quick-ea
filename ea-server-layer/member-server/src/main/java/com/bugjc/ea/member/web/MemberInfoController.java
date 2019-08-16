package com.bugjc.ea.member.web;

import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.member.service.MemberInfoService;
import com.bugjc.ea.member.web.http.body.memberinfo.GetBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * zipkin test
 * hystrix testø
 * @author qingyang
 */
@Slf4j
@RequestMapping("/info")
@RestController
public class MemberInfoController {

    @Resource
    private MemberInfoService memberInfoService;

    /**
     * 获取会员信息
     * @param requestBody
     * @return
     */
    @PostMapping(value = "/get")
    public Result get(@Validated @RequestBody GetBody.RequestBody requestBody) {
        return memberInfoService.get(requestBody);
    }
}
