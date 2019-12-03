package com.bugjc.ea.opensdk.http.core.aop;


import com.bugjc.ea.opensdk.http.core.aop.aspect.Aspect;
import com.bugjc.ea.opensdk.http.core.aop.interceptor.CgLibProxyInterceptor;
import com.bugjc.ea.opensdk.http.core.aop.interceptor.JdkProxyInterceptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProxyUtil {

    /**
     * 创建代理对象
     * @param target
     * @return
     */
    public static Object createProxy(Object target){
        try {
            return new CgLibProxyInterceptor().newProxy(target);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        log.info("使用JDK动态代理模式");
        return new JdkProxyInterceptor().newProxy(target);
    }


    /**
     * 创建切面代理对象
     * @param target
     * @param aspect
     * @return
     */
    public static Object createProxy(Object target, Aspect aspect){
        try {
            return new CgLibProxyInterceptor().newProxy(target, aspect);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        log.info("使用JDK动态代理模式");
        return new JdkProxyInterceptor().newProxy(target, aspect);
    }


}
