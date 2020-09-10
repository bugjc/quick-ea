package com.bugjc.ea.server.job.core.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.CommonResultCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 依赖第三方（内部）服务接口客户端
 * @author aoki
 */
@Slf4j
@Component
public class AccessPartyApiClient {


    @Data
    public static class HttpCallbackInfo{
        private String url;
        private String requestBody;

        /**
         * 参数转换
         * @param httpCallbackInfo
         * @return
         */
        public static HttpCallbackInfo convert(String httpCallbackInfo){
            return JSON.parseObject(httpCallbackInfo, HttpCallbackInfo.class);
        }
    }


    /**
     * 调用业务方接口
     * @param httpCallbackInfo
     * @return
     */
    public Result doPost(String httpCallbackInfo){
        try {
            //获取 http 参数回调对象信息
            AccessPartyApiClient.HttpCallbackInfo httpObject = AccessPartyApiClient.HttpCallbackInfo.convert(httpCallbackInfo);

            //调用接口
            String url = httpObject.getUrl();
            log.info("调用外部接口，URL:{}",url);
            HttpRequest httpRequest = HttpUtil.createPost(url);
            httpRequest.contentType("application/json;charset=utf-8");
            String result = httpRequest.timeout(8000).body(httpObject.getRequestBody()).execute().body();
            log.info("外部接口调用成功的应答数据：{}",result);
            return JSON.parseObject(result,Result.class);
        }catch (Exception ex){
            log.error("外部接口调用失败的应答数据：{}", ex.getMessage());
            return Result.failure(CommonResultCode.INTERNAL_SERVER_ERROR.getCode(), ex.getMessage());
        }

    }
}
