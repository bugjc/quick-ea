package com.bugjc.ea.member.service;

import feign.Param;
import feign.RequestLine;

/**
 * zipkin service test
 * @author qingyang
 */
public interface TestService {

    /**
     * 测试第一步
     * @param userId
     */
    @RequestLine("GET /test/zipkin/{userId}")
    String testZipkin(@Param("userId") String userId);

    /**
     * 测试断路器
     * @param userId
     */
    @RequestLine("GET /test/hystrix/{userId}")
    String testHystrix(@Param("userId") String userId);

}
