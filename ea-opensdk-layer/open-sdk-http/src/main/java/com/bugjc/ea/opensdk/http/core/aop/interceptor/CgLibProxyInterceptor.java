package com.bugjc.ea.opensdk.http.core.aop.interceptor;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.bugjc.ea.opensdk.http.core.aop.AspectInterceptorUtil;
import com.bugjc.ea.opensdk.http.core.aop.aspect.Aspect;
import com.bugjc.ea.opensdk.http.core.aop.aspect.DefaultAspect;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLib 动态代理
 * @author aoki
 * @date 2019/11/29
 * **/
public class CgLibProxyInterceptor implements MethodInterceptor {
    /** 需要代理的目标对象 */
    private Object target;
    /** 切面对象 */
    private Aspect aspect;

    /**
     * 将目标对象传入进行代理
     */
    public Object newProxy(Object target) {
        this.target = target;
        this.aspect = new DefaultAspect();
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(target.getClass().getInterfaces());
        enhancer.setCallback(this);
        // 返回代理对象
        return enhancer.create();
    }

    /**
     * 将目标对象传入进行代理
     */
    public Object newProxy(Object target, Class<? extends Aspect> aspectClass) {
        this.target = target;
        this.aspect = ReflectUtil.newInstance(aspectClass);;

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(target.getClass().getInterfaces());
        enhancer.setCallback(this);
        // 返回代理对象
        return enhancer.create();
    }


    @Override
    public Object intercept(Object proxy, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable {
        try {
            //获取并检查方法是否已开启切面拦截
            if (!AspectInterceptorUtil.check(method)){
                return method.invoke(target, args);
            }

            if (aspect.before(target, method, args)){
                Object returnVal = method.invoke(ClassUtil.isStatic(method) ? null : target, args);
                aspect.afterReturning(target, method, args, returnVal);
                return returnVal;
            }
        } catch (Exception ex){
            aspect.afterThrowing(target, method, args, ex.getCause());
            throw ex.getCause();
        } finally {
            aspect.after();
        }
        return null;
    }
}
