package com.bugjc.ea.opensdk.http.core.component.monitor.aspect;

import cn.hutool.core.date.TimeInterval;
import com.bugjc.ea.opensdk.http.core.aop.aspect.Aspect;
import com.bugjc.ea.opensdk.http.core.component.monitor.DisruptorConfig;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * http 调用切面
 * @author aoki
 * @date 2019/11/30
 * **/
@Slf4j
public class HttpCallAspect implements Aspect, Serializable {
    private static final long serialVersionUID = 1L;
    private TimeInterval interval = new TimeInterval();
    private HttpCallEvent metadata = null;

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        interval.start();
        return true;
    }

    @Override
    public void afterReturning(Object target, Method method, Object[] args, Object returnVal) {
        //设置元数据
        metadata = new HttpCallEvent();
        metadata.setId(String.valueOf(Arrays.hashCode(args)));
        metadata.setPath(String.valueOf(args[0]));
        metadata.setType(HttpCallEvent.TypeEnum.TotalRequests);
        metadata.setStatus(HttpCallEvent.StatusEnum.CallSuccess);
        metadata.setIntervalMs(interval.intervalMs());
        DisruptorConfig.getInstance().push(metadata);
    }

    @Override
    public void afterThrowing(Object target, Method method, Object[] args, Throwable e) {
        //设置元数据
        metadata = new HttpCallEvent();
        metadata.setId(String.valueOf(Arrays.hashCode(args)));
        metadata.setPath(String.valueOf(args[0]));
        metadata.setType(HttpCallEvent.TypeEnum.TotalRequests);
        metadata.setStatus(HttpCallEvent.StatusEnum.CallFailed);
        metadata.setIntervalMs(interval.intervalMs());
        DisruptorConfig.getInstance().push(metadata);
    }

    @Override
    public void after() {
        //及时清理对象
        interval = null;
        metadata = null;
    }
}
