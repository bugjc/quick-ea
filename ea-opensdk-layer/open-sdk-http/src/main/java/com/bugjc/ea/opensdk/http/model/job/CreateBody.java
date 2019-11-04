package com.bugjc.ea.opensdk.http.model.job;


import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建任务请求应答对象
 *
 * @author aoki
 * @date 2019/9/23 15:57
 **/
@Data
public class CreateBody implements Serializable {

    /**
     * 请求参数对象
     */
    @Data
    public static class RequestBody implements Serializable {
        //任务编号不能为空（业务方唯一key）
        private String jobId;
        //任务执行时间不能为空(格式：yyyy-MM-dd HH:mm:ss)
        private String execTime;
        //http 回调参数信息不能为空
        private String httpCallbackInfo;

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
        //业务执行失败应答码
        private int failCode = 0;
    }
}
