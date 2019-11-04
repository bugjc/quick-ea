package com.bugjc.ea.auth.web.io.platform.auth;

import lombok.Data;

import javax.validation.constraints.NotNull;
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
        @NotNull(message = "接口凭证不能为空")
        private String accessToken;
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
