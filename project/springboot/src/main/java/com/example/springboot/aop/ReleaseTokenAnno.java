package com.example.springboot.aop;

import com.example.springboot.annotation.ReleaseToken;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

//启动AOP  @Aspect 注解用来描述一个切面类，定义切面类的时候需要打上这个注解
@Aspect
//@Component 注解将该类交给 Spring 来管理
@Component
public class ReleaseTokenAnno {
    //切入点，通过注解切入到方法中
    @Pointcut("@annotation(com.example.springboot.annotation.ReleaseToken)")
    private void cut() {
    }
    //前置通知
    @Before("cut()")
    public void before() {
        System.out.println("注解设置成功");

    }
}
