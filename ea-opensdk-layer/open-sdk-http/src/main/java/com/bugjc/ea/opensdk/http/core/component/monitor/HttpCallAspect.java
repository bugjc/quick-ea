package com.bugjc.ea.opensdk.http.core.component.monitor;

import cn.hutool.core.date.TimeInterval;
import com.bugjc.ea.opensdk.http.core.aop.aspect.Aspect;
import com.bugjc.ea.opensdk.http.core.component.monitor.entity.CountInfoTable;
import com.bugjc.ea.opensdk.http.core.component.monitor.entity.HttpMetadata;
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

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        interval.start();
        return true;
    }

    @Override
    public void afterReturning(Object target, Method method, Object[] args, Object returnVal) {
        //设置元数据
        HttpMetadata metadata = new HttpMetadata();
        metadata.setId(String.valueOf(Arrays.hashCode(args)));
        metadata.setPath(String.valueOf(args[0]));
        metadata.setType(HttpMetadata.TypeEnum.TotalRequests);
        metadata.setStatus(HttpMetadata.StatusEnum.CallSuccess);
        if (HttpMonitorDataManage.getInstance().push(metadata)){
            log.info("收集监控数据成功！{}", method.getName());
        }
    }

    @Override
    public void afterThrowing(Object target, Method method, Object[] args, Throwable e) {

        //设置元数据
        HttpMetadata metadata = new HttpMetadata();
        metadata.setId(String.valueOf(Arrays.hashCode(args)));
        metadata.setPath(String.valueOf(args[0]));
        metadata.setType(HttpMetadata.TypeEnum.TotalRequests);
        metadata.setStatus(HttpMetadata.StatusEnum.CallFailed);
        if (HttpMonitorDataManage.getInstance().push(metadata)){
            log.info("收集监控数据成功！{}" ,method.getName());
        }
    }

    @Override
    public void after() {
        ThreadPoolExecutorUtil.execute(HttpMetadataHandle.getInstance());
        log.info("调用统计结果：{}",CountInfoTable.getInstance().getCountInfo());
    }
}
