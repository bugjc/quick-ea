package com.bugjc.ea.server.job.web.io.job;
import com.bugjc.ea.server.job.core.enums.business.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取任务类型列表请求应答对象
 * @author aoki
 * @date 2019/9/23 15:57
 **/
@Data
public class StatusListBody implements Serializable {

    /**
     * 应答参数对象
     */
    @Data
    public static class ResponseBody implements Serializable {

        //业务执行应答码
        private int failCode = 0;

        //任务状态列表
        private List<Status> statuses = enumToList();

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        static class Status implements Serializable {
            private String status;
            private String title;
        }

        /**
         * 枚举转换成集合
         * @return
         */
        List<Status> enumToList(){
            List<Status> types = new ArrayList<>();
            types.add(new Status("","请选择"));
            JobStatus[] jobStatuses = JobStatus.values();
            for (JobStatus jobStatus : jobStatuses) {
                types.add(new Status(String.valueOf(jobStatus.getStatus()), jobStatus.getDesc()));
            }
            return types;
        }
    }


}
