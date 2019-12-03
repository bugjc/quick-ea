package com.bugjc.ea.opensdk.http.core.aop.aspect;

import java.io.Serializable;
import java.lang.reflect.Method;

public class DefaultAspect implements Aspect, Serializable {

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        return true;
    }

    @Override
    public void afterReturning(Object target, Method method, Object[] args, Object returnVal) {}

    @Override
    public void afterThrowing(Object target, Method method, Object[] args, Throwable e) {}

    @Override
    public void after() {}
}
