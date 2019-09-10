package com.bugjc.ea.auth.core.filter;

import com.bugjc.ea.auth.core.constants.ApiGatewayKeyConstants;
import com.netflix.zuul.FilterProcessor;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 自定义过滤处理器，记录错误的过滤器
 */
public class CustomFilterProcessor extends FilterProcessor {

    @Override
    public Object processZuulFilter(ZuulFilter filter) throws ZuulException {
        try {
            return super.processZuulFilter(filter);
        } catch (ZuulException e) {
            RequestContext ctx = RequestContext.getCurrentContext();
            ctx.set(ApiGatewayKeyConstants.FAILED_FILTER, filter);
            throw e;
        }
    }
}
