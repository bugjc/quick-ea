package com.bugjc.ea.opensdk.test.service.impl;


import cn.hutool.http.HttpStatus;
import com.bugjc.ea.opensdk.test.service.CyclicBarrierTask;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认测试的业务逻辑
 */
@Slf4j
public class DefaultCyclicBarrierTaskImpl implements CyclicBarrierTask {

    @Override
    public int execTask() {
        log.info("默认性能测试执行的业务逻辑，请实现 CyclicBarrierTask 接口编写要测试的业务逻辑！");
        return HttpStatus.HTTP_OK;
    }
}
