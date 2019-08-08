package com.bugjc.ea.gateway.core.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.gateway.model.App;
import com.bugjc.ea.gateway.service.AppService;
import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.http.opensdk.core.dto.ResultGenerator;
import com.bugjc.ea.http.opensdk.core.util.ApiGatewayHttpClient;
import com.bugjc.ea.http.opensdk.model.AppParam;
import com.bugjc.ea.http.opensdk.service.HttpService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * @author aoki
 */
@Slf4j
@Component
public class JwtApiClient {

    @Data
    @Configuration
    class Config{
        @Value("${api-gateway.server.address}")
        private String apiServerAddress;
    }

    public static class ContentPath{

        //接口默认版本
        private final static String DEFAULT_VERSION = "1.0";
        //校验token
        public final static String VERIFY_TOKEN_PATH = "/jwt/verify";

        /**
         * 接口路径版本关系映射
         */
        private static Map<String,String> PATH_MAP_VERSION = new HashMap<>();
        static {
            /**
             * 校验token
             */
            PATH_MAP_VERSION.put(VERIFY_TOKEN_PATH,"2.0");
        }


    }

    @Resource
    private Config config;

    @Resource
    private AppService appService;

    /**
     * 接口调用
     * @param path
     * @param param
     * @return
     */
    public Result post(String appId, String path, JSONObject param){
        //获取应用配置信息
        App app = appService.findByAppId(appId);
        if (app == null){
            return ResultGenerator.genFailResult("无效的APP_ID");
        }

        //获取接口版本路径信息
        String version = ContentPath.PATH_MAP_VERSION.get(path);
        if (version == null){
            version = ContentPath.DEFAULT_VERSION;
        }

        String url = config.getApiServerAddress().concat(path);
        try {
            AppParam appParam = new AppParam();
            appParam.setBaseUrl(config.apiServerAddress);
            appParam.setRsaPrivateKey(app.getRsaPrivateKey());
            appParam.setRsaPublicKey(app.getRsaPublicKey());
            appParam.setAppId(app.getAppId());
            HttpService httpService = ApiGatewayHttpClient.getHttpService(appParam);
            String json = httpService.post(url,version,param.toJSONString());
            return JSON.parseObject(json,Result.class);
        } catch (Exception e) {
            log.info(e.getMessage(),e);
            return ResultGenerator.genFailResult(e.getMessage());
        }
    }

}
