package com.bugjc.ea.opensdk.aop.interceptor;

import com.bugjc.ea.opensdk.aop.AspectInterceptorUtil;
import com.bugjc.ea.opensdk.aop.aspect.Aspect;
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
     * @param target            --目标对象
     * @param aspect            --切面对象
     * @return  代理对象
     */
    public Object newProxy(Object target, Aspect aspect) {
        this.target = target;
        this.aspect = aspect;

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(target.getClass().getInterfaces());
        enhancer.setCallback(this);
        // 返回代理对象
        return enhancer.create();
    }

    /**
     * 调用目标方法
     * @param proxy             --代理对象
     * @param method            --目标待执行方法
     * @param args              --目标参数
     * @return  目标方法执行结果
     * @throws Throwable        --目标方法抛出的异常
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable {
        //获取并检查方法是否已开启切面拦截
        boolean flag = AspectInterceptorUtil.check(method);
        //获取并检查方法是否已开启切面拦截
        if (!flag){
            return method.invoke(target, args);
        }

        try {
            if (aspect.before(target, method, args)){
                Object returnVal = method.invoke(target, args);
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
