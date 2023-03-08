package com.example.springboot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//自定义注解

@Target({ElementType.METHOD}) // @Target注解用于定义注解的使用位置，使用在方法上
@Retention(RetentionPolicy.RUNTIME) // @Retention注解用于指明修饰的注解的生存周期，即会保留到哪个阶段
public @interface ReleaseToken {

}
