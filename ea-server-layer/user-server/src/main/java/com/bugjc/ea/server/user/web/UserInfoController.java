package com.bugjc.ea.server.user.web;

import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.server.user.service.MemberInfoService;
import com.bugjc.ea.server.user.web.io.info.GetBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * zipkin test
 * hystrix testø
 * @author qingyang
 */
@Slf4j
@RequestMapping("/info")
@RestController
public class UserInfoController {

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
