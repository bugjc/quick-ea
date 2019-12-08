package com.bugjc.ea.opensdk.http.core.util;

import com.bugjc.ea.opensdk.http.core.component.eureka.entity.Server;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RuleUtil {


    /**
     * 加权轮询
     */
    private static final AtomicInteger POS = new AtomicInteger(0);

    /**
     * 加权轮询
     * @param list
     * @return
     */
    public static Server weightRobin(List<Server> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int index = POS.get();
        if (index >= list.size()){
            POS.set(0);
            index = 0;
        }
        Server server = list.get(index);
        POS.getAndIncrement();
        return server;
    }

}
