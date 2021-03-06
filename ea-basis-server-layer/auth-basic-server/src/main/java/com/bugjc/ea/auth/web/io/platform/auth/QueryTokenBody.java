package com.bugjc.ea.auth.web.io.platform.auth;

import lombok.Data;

import javax.validation.constraints.NotNull;
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
        //运营商标识
        @NotNull(message = "应用编号不能为空！")
        private String appId;
        //运营商密钥
        @NotNull(message = "应用密钥不能为空")
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
        private int failCode;
        //获取的凭证
        private String accessToken;
        //凭证有效期
        private String tokenAvailableTime;
    }
}
