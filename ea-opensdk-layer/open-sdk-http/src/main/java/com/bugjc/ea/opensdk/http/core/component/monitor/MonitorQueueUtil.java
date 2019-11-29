package com.bugjc.ea.opensdk.http.core.component.monitor;

import cn.hutool.core.collection.BoundedPriorityQueue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author aoki
 */
public class MonitorQueueUtil {


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Param implements Serializable {
        private String requestId;
        private String requestData;
    }

    /**
     * 队列
     */
    private BoundedPriorityQueue<Param> chargeMapQueue = new BoundedPriorityQueue<Param>(100000, (o1, o2) -> o1 == o2 ? 0 : -1);

    public BoundedPriorityQueue<Param> getChargeMapQueue(){
        return chargeMapQueue;
    }

}
