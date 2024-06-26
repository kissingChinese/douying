package com.example.limit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

    int limit() default 0;

    long time() default 0;

    String key() default "";

    String msg() default "系统服务繁忙";
}
