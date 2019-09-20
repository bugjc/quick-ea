package com.bugjc.ea.server.user.service.impl;

import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.ResultGenerator;
import com.bugjc.ea.server.user.service.MemberInfoService;
import com.bugjc.ea.server.user.web.io.info.GetBody;
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
public class MemberInfoServiceImpl implements MemberInfoService {


    @NewSpan
    @Override
    public Result get(GetBody.RequestBody requestBody) {
        return ResultGenerator.genSuccessResult(new GetBody.ResponseBody("aoki","18"));
    }


    /** 调用链注解最好放到实现类，防止实现类内部相互调用不走接口出现错误 **/
    @NewSpan
    private void add(@SpanTag("UserID") String userId) {
        log.info("add:"+userId);
    }

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
