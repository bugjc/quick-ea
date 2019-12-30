package com.bugjc.ea.opensdk.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启切面拦截
 * @author aoki
 * @date 2019/12/4
 * **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Before {
}
