package com.bugjc.ea.opensdk.aop.aspect;

import java.lang.reflect.Method;

public class DefaultAspect implements Aspect{

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        System.out.println("--前置通知--");
        return true;
    }

    @Override
    public void afterReturning(Object target, Method method, Object[] args, Object returnVal) {
        System.out.println("--后置通知--");
    }

    @Override
    public void afterThrowing(Object target, Method method, Object[] args, Throwable e) {
        System.out.println("--异常通知--");
    }

    @Override
    public void after() {
        System.out.println("--最终通知--");
    }
}