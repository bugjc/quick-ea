package com.bugjc.ea.qrcode.core.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author qingyang
 * @date 2018/7/29 00:48
 */
@FeignClient(name = "member-server")
public interface MemberApi {

    /**
     * 测试第一步
     * @param userId
     */
    @GetMapping(value = "/zipkin/test/{userId}")
    void stepOne(String userId);
}
