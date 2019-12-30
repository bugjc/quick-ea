package com.bugjc.ea.opensdk.aop.aspect;

import java.lang.reflect.Method;

/**
 * 切面接口
 * @author aoki
 * @date 2019/12/3
 * **/
public interface Aspect {

    /**
     * 前置通知：在目标方法执行之前执行
     * @param target        --目标对象
     * @param method        --目标方法
     * @param args          --目标参数
     * @return  是否继续执行接下来的操作 true:继续执行
     */
    boolean before(Object target, Method method, Object[] args);

    /**
     * 后置通知：在目标方法执行之后执行
     *          可以获取目标方法的返回值
     *          当目标方法遇到异常，不执行
     * @param target        --目标对象
     * @param method        --目标返回
     * @param args          --目标参数
     * @param returnVal     --目标返回值
     */
    void afterReturning(Object target, Method method, Object[] args, Object returnVal);

    /**
     * 异常通知：获取目标方法抛出的异常
     * @param target 目标对象
     * @param method 目标方法
     * @param args   目标参数
     * @param e      目标抛出的异常
     */
    void afterThrowing(Object target, Method method, Object[] args, Throwable e);


    /**
     * 最终通知：无论目标方法是否遇到异常都会执行，相当于代码中的 finally
     */
    void after();

}
