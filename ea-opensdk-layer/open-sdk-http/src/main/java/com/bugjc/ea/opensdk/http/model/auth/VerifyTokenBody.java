package com.bugjc.ea.opensdk.http.model.auth;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * 校验 token接口请求响应参数对象
 * @author aoki
 * @date ${date}
 */
@Data
public class VerifyTokenBody implements Serializable {

    /**
     * 请求参数对象
     */
    @Data
    public static class RequestBody implements Serializable {
        //接口凭证
        private String accessToken;

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
        //失败原因
        private int failCode;
    }
}
