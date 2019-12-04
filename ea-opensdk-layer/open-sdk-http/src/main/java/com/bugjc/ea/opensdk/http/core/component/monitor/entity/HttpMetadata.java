package com.bugjc.ea.opensdk.http.core.component.monitor.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class HttpMetadata implements Serializable {
    /**
     * 标识ID
     */
    private String id;

    /**
     * 接口路径
     */
    private String path;

    /**
     * 接口状态
     */
    private StatusEnum status;

    /**
     * 指标类型
     */
    private TypeEnum type;

    public HttpMetadata(){
        this.id = "default";
        this.path = "/";
        this.status = StatusEnum.Ready;
        this.type = TypeEnum.TotalRequests;
    }

    /**
     * 元数据状态
     * @author aoki
     */
    public enum StatusEnum {
        /**
         * 状态
         */
        Ready(0,"准备就绪"),
        CallSuccess(1,"调用成功"),
        CallFailed(2,"调用失败");

        private final int status;
        private final String desc;

        StatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public int getStatus() {
            return status;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 元数据指标
     * @author aoki
     */
    public enum TypeEnum {
        /**
         * 指标
         */
        TotalRequests(0,"总请求数");

        private final int type;
        private final String desc;

        TypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public int getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}