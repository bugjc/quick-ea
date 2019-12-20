package com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.impl;

import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;

/**
 * 总请求数指标服务实现
 * @author aoki
 * @date 2019/12/20
 * **/
public class TotalRequestCounterServiceImpl extends CounterDefaultServiceImpl {
    
    public TotalRequestCounterServiceImpl() {
        super(CounterKey.TotalRequests);
    }
}
