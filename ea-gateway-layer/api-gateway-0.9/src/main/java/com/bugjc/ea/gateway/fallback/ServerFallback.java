package com.bugjc.ea.gateway.fallback;

import com.bugjc.ea.gateway.core.dto.ResultGenerator;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 向所有路由发起请求失败时的回滚处理
 * @author qingyang
 * @date 2018/7/31 17:22
 */
@Component
public class ServerFallback implements FallbackProvider {
    @Override
    public String getRoute() {
        //表示所有服务，如果想对单个服务进行限制，写入服务ID即可。
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse(){

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                //和body中的内容编码一致，否则容易乱码
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream(ResultGenerator.genFailResult("系统繁忙，请稍后再试。").toString().getBytes("UTF-8"));
            }

            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            /**
             * 网关向api服务请求是失败了，但是消费者客户端向网关发起的请求是OK的，
             * 不应该把api的404,500等问题抛给客户端
             * 网关和api服务集群对于客户端来说是黑盒子
             */
            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.OK.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.OK.getReasonPhrase();
            }

            @Override
            public void close() {

            }
        };
    }
}
