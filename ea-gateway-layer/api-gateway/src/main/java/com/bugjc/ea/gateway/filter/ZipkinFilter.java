package com.bugjc.ea.gateway.filter;

import brave.Tracer;
import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * 自定义链路数据
 * @author qingyang
 */
@Slf4j
@Component
public class ZipkinFilter extends ZuulFilter {

    private final Tracer tracer;

    @Autowired
    public ZipkinFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 900;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        //通过Tracer在链路数据中添加自定义数据-操作人
        tracer.currentSpan().tag("operator","aoki");
        log.info(tracer.currentSpan().context().traceIdString());
        return null;
    }
}
