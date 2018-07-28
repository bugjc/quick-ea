package com.bugjc.ea.member.service.impl;

import com.bugjc.ea.member.service.ZipkinService;
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
public class ZipkinServiceImpl implements ZipkinService {

    /** 调用链注解最好放到实现类，防止实现类内部相互调用不走接口出现错误 **/
    @NewSpan
    @Override
    public void stepOne(@SpanTag("UserID") String userId) {
      log.info("orderStep1Method,param:"+userId);
      this.stepTwo(userId);
    }

    @NewSpan
    @Override
    public void stepTwo(@SpanTag("UserID") String userId) {
        log.info("orderStep2Method,param:"+userId);
    }
}
