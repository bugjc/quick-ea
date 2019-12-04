package com.bugjc.ea.opensdk.http.core.aop;

import java.lang.reflect.Method;

/**
 * 切面拦截器
 * @author aoki
 * @date 2019/12/4
 * **/
public class AspectInterceptorUtil {

    /**
     * 检查方法是否已开启切面拦截
     * @param method
     * @return true:开启切面拦截，false:未开启切面拦截
     */
    public static boolean check(Method method){
        //获取并检查方法是否已开启切面拦截
        return method.getAnnotation(AspectInterceptor.class) != null;
    }

}
