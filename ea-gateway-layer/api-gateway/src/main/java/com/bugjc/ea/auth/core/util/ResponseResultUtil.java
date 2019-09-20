package com.bugjc.ea.auth.core.util;

import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.dto.ResultGenerator;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

/**
 * @author aoki
 */
@Slf4j
public class ResponseResultUtil {

    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json;charset=UTF-8";


    public static void genSuccessResult(RequestContext ctx, String message) {
        log.info("{},将请求向后转发",message);
        ctx.setSendZuulResponse(true);
        ctx.setResponseStatusCode(200);
        ctx.set("isSuccess",true);
        ctx.getResponse().setContentType(CONTENT_TYPE_APPLICATION_JSON);
    }

    public static void genSuccessResult(RequestContext ctx, String message, String body) {
        log.info("{},将请求向后转发",message);
        ctx.setSendZuulResponse(true);
        ctx.setResponseStatusCode(200);
        ByteArrayInputStream bodyStream = new ByteArrayInputStream(body.getBytes());
        ctx.setResponseDataStream(bodyStream);
        ctx.setResponseBody(body);
        ctx.set("isSuccess",true);
        ctx.getResponse().setContentType(CONTENT_TYPE_APPLICATION_JSON);
    }

    public static void genErrorResult(RequestContext ctx, int status, String message){
        log.error("响应错误信息:{}",message);
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(200);
        ctx.setResponseBody(JSON.toJSONString(ResultGenerator.genFailResult(status,message)));
        ctx.set("isSuccess",false);
        ctx.getResponse().setContentType(CONTENT_TYPE_APPLICATION_JSON);
    }
}
