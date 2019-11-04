package com.bugjc.ea.opensdk.http.model.auth;

import com.alibaba.fastjson.JSON;
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
    public static class RequestBody implements Serializable {
        //应用编号
        private String appId;
        //应用密钥
        private String appSecret;

        @Override
        public String toString(){
            return JSON.toJSONString(this);
        }
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
        private int failCode;
        //获取的凭证
        private String accessToken;
        //凭证有效期
        private String tokenAvailableTime;
    }
}
