package com.bugjc.ea.server.job.web.io.job;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 删除任务信息请求应答对象
 * @author aoki
 * @date 2019/9/23 15:57
 **/
@Data
public class DelBody implements Serializable {

    /**
     * 请求参数对象
     */
    @Data
    public static class RequestBody implements Serializable{
        @NotNull(message = "任务编号不能为空（业务方唯一key）")
        private String jobId;
    }

    /**
     * 应答参数对象
     */
    @Data
    public static class ResponseBody implements Serializable{
        //业务执行失败应答码
        private int failCode = 0;
    }
}
