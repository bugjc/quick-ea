package com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;

/**
 * 成功请求总数指标服务实现
 * @author aoki
 * @date 2019/12/20
 * **/
public class SuccessRequestCounterServiceImpl extends CounterDefaultServiceImpl {

    public SuccessRequestCounterServiceImpl() {
        super(CounterKey.SuccessRequests);
    }
}
