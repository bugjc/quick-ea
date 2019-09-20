package com.bugjc.ea.server.user.web.io.balance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 余额支付请求应答体
 * @author aoki
 */
@Data
public class PayBody implements Serializable {

    /**
     * 请求参数对象
     */
    @Data
    public static class RequestBody implements Serializable{
        @NotBlank(message = "用户ID不能为空")
        private String userId;
        @NotBlank(message = "消费金额不能为空！（单位：分）")
        private int amt;
    }

    /**
     * 应答参数对象
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseBody implements Serializable{
        //业务失败码
        private int failCode = 0;
    }

}
