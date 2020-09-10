package com.bugjc.ea.gateway.zuul.core.api;

import com.bugjc.ea.gateway.zuul.core.dto.ApiGatewayServerResultCode;
import com.bugjc.ea.gateway.zuul.model.App;
import com.bugjc.ea.gateway.zuul.service.AppService;
import com.bugjc.ea.opensdk.http.api.AuthPathInfo;
import com.bugjc.ea.opensdk.http.core.dto.CommonResultCode;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.util.HttpClient;
import com.bugjc.ea.opensdk.http.model.AppParam;
import com.bugjc.ea.opensdk.http.model.auth.VerifyTokenBody;
import com.bugjc.ea.opensdk.http.service.AuthService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 授权认证服务
 *
 * @author aoki
 * @date 2019/11/4
 **/
@Slf4j
@Component
public class AuthApiClient {

    @Data
    @Configuration
    static class Config {
        @Value("${api-gateway.server.address}")
        private String apiServerAddress;
    }

    @Resource
    private Config config;

    @Resource
    private AppService appService;

    /**
     * 接口调用
     *
     * @param appId
     * @param token
     * @return
     */
    public Result verifyToken(String appId, String token) {
        //获取应用配置信息
        App app = appService.findByAppId(appId);
        if (app == null) {
            return Result.failure(ApiGatewayServerResultCode.APP_ID_MISSING);
        }

        try {
            AppParam appParam = new AppParam();
            appParam.setBaseUrl(config.apiServerAddress);
            appParam.setRsaPrivateKey(app.getRsaPrivateKey());
            appParam.setRsaPublicKey(app.getRsaPublicKey());
            appParam.setAppSecret(app.getAppSecret());
            appParam.setAppId(app.getId());
            AuthService authService = HttpClient.getInstance().getAuthService(appParam);
            //调用接口
            VerifyTokenBody.RequestBody requestBody = new VerifyTokenBody.RequestBody();
            requestBody.setAccessToken(token);
            return authService.verifyToken(AuthPathInfo.VERIFY_TOKEN_V1, requestBody);
        } catch (Exception e) {
            log.info("校验 Token 发生错误：{}", e.getMessage(), e);
            return Result.failure(CommonResultCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }


}
