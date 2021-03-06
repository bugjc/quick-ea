package com.bugjc.ea.gateway.zuul.core.util;

import cn.hutool.core.util.RandomUtil;
import com.bugjc.ea.gateway.zuul.model.Server;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RuleUtil {

    /**
     * 加权随机
     * @param list
     * @return
     * @throws MalformedURLException
     */
    private static Server random(List<Server> list) throws MalformedURLException {
        return list.get(RandomUtil.randomInt(list.size()));
    }

    /**
     * 加权轮询
     */
    private static final AtomicInteger POS = new AtomicInteger(0);

    public static Server weightRobin(List<Server> list) throws MalformedURLException {
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
