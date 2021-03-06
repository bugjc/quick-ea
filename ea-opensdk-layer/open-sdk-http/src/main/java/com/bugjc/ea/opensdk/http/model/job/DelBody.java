package com.bugjc.ea.opensdk.http.model.job;



import com.alibaba.fastjson.JSON;
import lombok.Data;

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
        //任务编号不能为空（业务方唯一key）
        private String jobId;

        @Override
        public String toString(){
            return JSON.toJSONString(this);
        }
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
