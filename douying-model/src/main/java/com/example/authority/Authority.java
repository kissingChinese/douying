package com.example.authority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**

 * CreateTime: 2022-06-05 00:37
 * 授权注解
 * 不需要校验的请求不用加注解即可
 */


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authority {

    /**权限标识*/
    String[] value();

    /**具体执行校验类*/
    Class verify() default DefaultAuthorityVerify.class;
}
