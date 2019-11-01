package com.bugjc.ea.opensdk.test.service;

/**
 * 使用 CyclicBarrierComponent 组件的前提是实现当前接口，方法里写的是具体的业务执行逻辑
 */
public interface CyclicBarrierTask {

    /**
     * 执行任务
     * @return 需符合 http status 定义标准
     */
    int execTask();
}
