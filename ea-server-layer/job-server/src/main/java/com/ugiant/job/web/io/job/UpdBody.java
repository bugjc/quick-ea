package com.ugiant.job.web.io.job;



import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改任务信息请求应答对象
 * @author aoki
 * @date 2019/9/23 15:57
 **/
@Data
public class UpdBody implements Serializable {

    /**
     * 请求参数对象
     */
    @Data
    public static class RequestBody implements Serializable{
        @NotNull(message = "任务编号不能为空（业务方唯一key）")
        private String jobId;
        @NotNull(message = "任务执行时间不能为空(格式：yyyy-MM-dd HH:mm:ss)")
        private String execTime;
        @NotNull(message = "http回调参数信息不能为空")
        private String httpCallbackInfo;
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
