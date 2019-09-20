package com.bugjc.ea.server.message.service;

import com.bugjc.ea.server.message.Tester;
import com.bugjc.ea.server.message.core.task.RetryComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author aoki
 */
@Slf4j
public class RetryComponentTest extends Tester {
    @Resource
    private RetryComponent retryComponent;

    /**
     * 查询充电站列表信息接口
     * @throws Exception
     */
    @Test
    public void testBalancePay() throws Exception {
        String userId = "1001";
        int amt = 100;
        retryComponent.run(userId,amt);
        Thread.sleep(10000);
    }
}
