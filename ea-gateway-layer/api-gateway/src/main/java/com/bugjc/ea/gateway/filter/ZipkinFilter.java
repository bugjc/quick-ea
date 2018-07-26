package com.bugjc.ea.gateway.filter;

import brave.Tracer;
import com.netflix.zuul.ZuulFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * 链路中自定义数据
 * @Auther: qingyang
 * @Date: 2018/7/25 17:51
 * @Description:
 */
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
        System.out.println(tracer.currentSpan().context().traceIdString());
        return null;
    }
}
