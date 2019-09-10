package com.bugjc.ea.auth.core.util;

import com.alibaba.fastjson.JSON;
import com.bugjc.ea.auth.core.constants.ApiGatewayKeyConstants;
import com.bugjc.ea.http.opensdk.core.dto.ResultGenerator;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 过滤链返回结果工具
 * @author aoki
 */
@Slf4j
public class FilterChainReturnResultUtil {

    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json;charset=UTF-8";


    /**
     * 表示当前过滤请求可以向下传播
     * @param ctx
     * @param message
     */
    public static void genSuccessResult(RequestContext ctx, String message) {
        log.info("{},将请求向后转发",message);
        ctx.setSendZuulResponse(true);
        ctx.setResponseStatusCode(200);
        ctx.set(ApiGatewayKeyConstants.IS_SUCCESS,true);
        ctx.getResponse().setContentType(CONTENT_TYPE_APPLICATION_JSON);
    }

    /**
     * 表示当前过滤请求止步于此
     * @param ctx
     * @param code
     * @param message
     */
    public static void genErrorResult(RequestContext ctx, int code, String message){
        String result = JSON.toJSONString(ResultGenerator.genFailResult(code,message));
        log.info("过滤器应答结果:{}",result);
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(200);
        ctx.setResponseBody(result);
        ctx.set(ApiGatewayKeyConstants.IS_SUCCESS,false);
        ctx.getResponse().setContentType(CONTENT_TYPE_APPLICATION_JSON);
    }
}
