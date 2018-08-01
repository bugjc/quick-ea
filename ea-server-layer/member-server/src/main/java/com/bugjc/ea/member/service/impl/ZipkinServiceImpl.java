package com.bugjc.ea.member.service.impl;

import com.bugjc.ea.member.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;

/**
 * zipkin service test
 * @author qingyang
 */
@Slf4j
@Service
public class ZipkinServiceImpl implements TestService {

    /** 调用链注解最好放到实现类，防止实现类内部相互调用不走接口出现错误 **/
    @NewSpan
    private void add(@SpanTag("UserID") String userId) {
        log.info("add:"+userId);
    }

    @NewSpan
    @Override
    public String testZipkin(String userId) {
        log.info("test zipkin:"+userId);
        this.add(userId);
        return "zipkin:"+userId;
    }

    @Override
    public String testHystrix(String userId) {
        log.info("test hystrix");
        return "hystrix:"+userId;
    }
}
