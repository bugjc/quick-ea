package com.bugjc.ea.auth.core.fallback;

import com.bugjc.ea.auth.core.dto.ApiGatewayResultCode;
import com.bugjc.ea.opensdk.http.core.constants.HttpHeaderKeyConstants;
import com.bugjc.ea.opensdk.http.core.dto.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 向所有路由发起请求失败时的回滚处理
 * @author qingyang
 * @date 2018/7/31 17:22
 */
@Slf4j
@Component
public class GatewayServerFallback implements FallbackProvider {
    @Override
    public String getRoute() {
        //表示所有服务，如果想对单个服务进行限制，写入服务ID即可。
        return "api-gateway-server";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        //失败应答消息
        String failureResponseMessage = ResultGenerator.genFailResult(ApiGatewayResultCode.BUSINESS_LOAD_BALANCING_UNAVAILABLE.getCode(),ApiGatewayResultCode.BUSINESS_LOAD_BALANCING_UNAVAILABLE.getMessage()).toString();
        return new ClientHttpResponse(){

            @NotNull
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                //和body中的内容编码一致，否则容易乱码
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                //标记失败回退
                headers.add(HttpHeaderKeyConstants.GATEWAY_ERROR_FLAG, "true");
                return headers;
            }

            @NotNull
            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream(failureResponseMessage.getBytes(StandardCharsets.UTF_8));
            }

            @NotNull
            @Override
            public HttpStatus getStatusCode() {
                return HttpStatus.OK;
            }

            /**
             * 网关向api服务请求是失败了，但是消费者客户端向网关发起的请求是OK的，
             * 不应该把api的404,500等问题抛给客户端
             * 网关和api服务集群对于客户端来说是黑盒子
             */
            @Override
            public int getRawStatusCode() {
                return HttpStatus.OK.value();
            }

            @NotNull
            @Override
            public String getStatusText() {
                return HttpStatus.OK.getReasonPhrase();
            }

            @Override
            public void close() {
            }
        };
    }
}
