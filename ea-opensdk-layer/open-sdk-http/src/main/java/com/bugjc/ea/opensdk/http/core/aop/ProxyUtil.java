package com.bugjc.ea.opensdk.http.core.aop;


import com.bugjc.ea.opensdk.http.core.aop.aspect.Aspect;
import com.bugjc.ea.opensdk.http.core.aop.interceptor.CgLibProxyInterceptor;
import com.bugjc.ea.opensdk.http.core.aop.interceptor.JdkProxyInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理工具类
 * @author aoki
 * @date 2019/12/4
 * **/
@Slf4j
public class ProxyUtil {
    

    /**
     * 创建切面代理对象
     * @param target        --目标对象
     * @param aspect        --切面对象
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
