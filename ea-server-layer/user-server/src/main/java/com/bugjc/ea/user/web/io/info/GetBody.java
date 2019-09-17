package com.bugjc.ea.user.web.io.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class GetBody implements Serializable {

    /**
     * 主动查询乘车及扣费记录接口请求参数对象
     */
    @Data
    public static class RequestBody implements Serializable{
        @NotBlank(message = "用户ID不能为空")
        private String userId;
    }

    /**
     * 主动查询乘车及扣费记录接口应答参数对象
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseBody implements Serializable{
        private String nickname;
        private String age;
    }

}
