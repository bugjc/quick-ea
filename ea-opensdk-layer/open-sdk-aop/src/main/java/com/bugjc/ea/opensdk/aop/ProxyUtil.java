package com.bugjc.ea.opensdk.aop;

import com.bugjc.ea.opensdk.aop.aspect.Aspect;
import com.bugjc.ea.opensdk.aop.interceptor.CgLibProxyInterceptor;
import com.bugjc.ea.opensdk.aop.interceptor.JdkProxyInterceptor;

/**
 * 代理工具类
 * @author aoki
 * @date 2019/12/4
 * **/
public class ProxyUtil {
    

    /**
     * 创建切面代理对象
     * @param target        --目标对象
     * @param aspect        --切面对象
     * @return   代理对象
     */
    public static Object createProxy(Object target, Aspect aspect){
        try {
            return new CgLibProxyInterceptor().newProxy(target, aspect);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        return new JdkProxyInterceptor().newProxy(target, aspect);
    }


}
