package com.bugjc.ea.server.websocket.component;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计数器组件
 *
 * @author aoki
 * @date 2019/9/27 9:40
 **/
public class AtomicCounterComponent {

    private static AtomicInteger counter = new AtomicInteger(0);

    /**
     * 自增
     */
    public static void increment() {
        counter.getAndIncrement();
    }

    /**
     * 自减
     */
    public static void decrement() {
        counter.getAndDecrement();
    }


    /**
     * 获取计数值
     *
     * @return
     */
    public static int get() {
        return counter.get();
    }
}
