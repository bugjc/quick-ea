package com.bugjc.ea.opensdk.test;

import com.bugjc.ea.opensdk.test.component.CyclicBarrierComponent;
import com.bugjc.ea.opensdk.test.service.CyclicBarrierTask;

/**
 * 构建API
 * @author aoki
 */
public class TestBuilder {

    private int total;
    private CyclicBarrierComponent cyclicBarrierComponent = new CyclicBarrierComponent();


    /**
     * 设置请求总数
     * @param total                 -- 任务总线程数,一个线程对应一个请求
     * @return
     */
    public TestBuilder setTotal(int total) {
        this.total = total;
        this.cyclicBarrierComponent.setTotal(total);
        return this;
    }

    /**
     * 设置要测试的业务逻辑对象
     * @param cyclicBarrierTask     --
     * @return
     */
    public TestBuilder setCyclicBarrierTask(CyclicBarrierTask cyclicBarrierTask) {
        this.cyclicBarrierComponent.setCyclicBarrierTask(cyclicBarrierTask);
        return this;
    }

    /**
     * 构建http调用对象
     * @return
     */
    public CyclicBarrierComponent build(){
        if (this.total == 0){
            throw new IllegalStateException("The total number of threads is not set");
        } else {
            return this.cyclicBarrierComponent;
        }
    }

}
