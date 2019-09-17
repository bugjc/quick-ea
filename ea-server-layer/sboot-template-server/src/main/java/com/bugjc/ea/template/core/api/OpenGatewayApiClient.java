package com.bugjc.ea.template.core.api;

import cn.hutool.core.bean.BeanUtil;
import com.bugjc.ea.http.opensdk.ApiBuilder;
import com.bugjc.ea.http.opensdk.model.AppParam;
import com.bugjc.ea.http.opensdk.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
/**
 * 依赖第三方（内部）服务接口客户端
 * @author aoki
 */
@Slf4j
@Component
public class OpenGatewayApiClient {


    @Data
    @Configuration
    class Config{
        @Value("${gateway.server.base-url}")
        private String baseUrl;
        @Value("${gateway.server.rsa-public-key}")
        private String rsaPublicKey;
        @Value("${gateway.server.rsa-private-key}")
        private String rsaPrivateKey;
        @Value("${gateway.server.app-id}")
        private String appId;
        @Value("${gateway.server.app-secret}")
        private String appSecret;
    }


    @Resource
    private Config config;

    /**
     * 获取http服务对象
     * @return
     */
    public UserService getUserHttpService() {
        AppParam appParam = new AppParam();
        BeanUtil.copyProperties(config,appParam);
        return new ApiBuilder()
                .setAppParam(appParam)
                .setHttpConnTimeout(5000)
                .buildUserApi();
    }
}
