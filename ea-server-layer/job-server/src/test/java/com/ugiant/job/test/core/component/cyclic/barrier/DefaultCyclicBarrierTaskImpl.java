package com.ugiant.job.test.core.component.cyclic.barrier;

import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认测试的业务逻辑
 */
@Slf4j
public class DefaultCyclicBarrierTaskImpl implements CyclicBarrierTask{

    @Override
    public int execTask() {
        log.info("默认测试的业务逻辑！");
        return HttpStatus.HTTP_OK;
    }
}
