package com.bugjc.ea.template.service;

import cn.hutool.core.date.DateUtil;
import com.bugjc.ea.template.Tester;
import com.bugjc.ea.template.core.task.RetryComponent;
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
