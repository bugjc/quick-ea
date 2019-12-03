package com.bugjc.ea.opensdk.http.core.component.monitor;

import cn.hutool.core.date.TimeInterval;
import com.bugjc.ea.opensdk.http.core.aop.aspect.Aspect;
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
    private final static String AOP_METHOD = "post";

    /**
     * 检查方法是否需要AOP
     * @param methodName
     * @return
     */
    private boolean checkAopMethod(String methodName){
        return methodName.equals(AOP_METHOD);
    }

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        if (!checkAopMethod(method.getName())){
            return true;
        }

        interval.start();
        //设置元数据
        HttpMetadata metadata = new HttpMetadata();
        metadata.setId(String.valueOf(Arrays.hashCode(args)));
        metadata.setPath(String.valueOf(args[0]));
        metadata.setType(HttpMetadata.TypeEnum.TotalRequests);
        metadata.setStatus(HttpMetadata.StatusEnum.Ready);
        if (!HttpMonitorDataManage.getInstance().add(metadata)){
            log.info("收集监控数据错误！");
        }else {
            log.info("收集监控数据成功！"+method.getName());
        }

        return true;
    }

    @Override
    public void afterReturning(Object target, Method method, Object[] args, Object returnVal) {

        if (!checkAopMethod(method.getName())){
            return;
        }

        //设置元数据
        HttpMetadata metadata = new HttpMetadata();
        metadata.setId(String.valueOf(Arrays.hashCode(args)));
        metadata.setPath(String.valueOf(args[0]));
        metadata.setType(HttpMetadata.TypeEnum.TotalRequests);
        metadata.setStatus(HttpMetadata.StatusEnum.CallSuccess);
        if (!HttpMonitorDataManage.getInstance().add(metadata)){
            log.info("收集监控数据错误！");
        }else {
            log.info("收集监控数据成功！"+method.getName());
        }
    }

    @Override
    public void afterThrowing(Object target, Method method, Object[] args, Throwable e) {
        if (!checkAopMethod(method.getName())){
            return;
        }

        //设置元数据
        HttpMetadata metadata = new HttpMetadata();
        metadata.setId(String.valueOf(Arrays.hashCode(args)));
        metadata.setPath(String.valueOf(args[0]));
        metadata.setType(HttpMetadata.TypeEnum.TotalRequests);
        metadata.setStatus(HttpMetadata.StatusEnum.CallFailed);
        HttpMonitorDataManage.getInstance().add(metadata);
        if (!HttpMonitorDataManage.getInstance().add(metadata)){
            log.info("收集监控数据错误！{}",metadata.toString());
        }else {
            log.info("收集监控数据成功！"+method.getName());
        }
    }

    @Override
    public void after() {}
}
