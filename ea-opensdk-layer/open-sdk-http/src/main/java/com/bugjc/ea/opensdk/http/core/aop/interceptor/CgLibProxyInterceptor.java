package com.bugjc.ea.opensdk.http.core.aop.interceptor;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.bugjc.ea.opensdk.http.core.aop.AspectInterceptorUtil;
import com.bugjc.ea.opensdk.http.core.aop.aspect.Aspect;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLib 动态代理
 * @author aoki
 * @date 2019/11/29
 * **/
@Slf4j
public class CgLibProxyInterceptor implements MethodInterceptor {
    /** 需要代理的目标对象 */
    private Object target;
    /** 切面对象 */
    private Aspect aspect;

    /**
     * 将目标对象传入进行代理
     * @param target            --目标对象
     * @param aspectClass       --切面对象
     * @return  代理对象
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
        try {
            //获取并检查方法是否已开启切面拦截
            if (!flag){
                log.debug("CGLib 动态代理方法 {} 无需拦截！", method.getName());
                return method.invoke(target, args);
            }

            if (aspect.before(target, method, args)){
                Object returnVal = method.invoke(ClassUtil.isStatic(method) ? null : target, args);
                aspect.afterReturning(target, method, args, returnVal);
                return returnVal;
            }
        } catch (Exception ex){
            if (flag){
                aspect.afterThrowing(target, method, args, ex.getCause());
            }
            throw ex.getCause();
        } finally {
            if (flag){
                aspect.after();
            }
        }
        return null;
    }
}
