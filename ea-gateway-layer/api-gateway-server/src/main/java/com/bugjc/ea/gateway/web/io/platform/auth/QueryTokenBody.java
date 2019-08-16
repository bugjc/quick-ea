package com.bugjc.ea.gateway.web.io.platform.auth;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

/**
 * 获取 token接口请求响应参数对象
 * @author aoki
 * @date ${date}
 */
@Data
public class QueryTokenBody implements Serializable {

    /**
     * 请求参数对象
     */
    @Data
    @JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
    public static class RequestBody implements Serializable {
        //运营商标识
        private String appId;
        //运营商密钥
        private String appSecret;
    }

    /**
     * 应答参数对象
     */
    @Data
    public static class ResponseBody implements Serializable {
        //应用ID
        private String appId;
        //成功状态
        private int successStat;
        //失败原因
        private int failReason;
        //获取的凭证
        private String accessToken;
        //凭证有效期
        private String tokenAvailableTime;
    }
}
