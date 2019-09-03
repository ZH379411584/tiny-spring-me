package com.jxufe.study.tinyspring.aop.framework.adapter;

import com.jxufe.study.tinyspring.aop.MethodBeforeAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 15:20
 **/
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {
    private MethodBeforeAdvice advice;

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
        this.advice = advice;
    }

    public Object invoke(MethodInvocation mi) throws Throwable {
        this.advice.before(mi.getMethod(),mi.getArguments(),mi.getThis());
        return mi.proceed();
    }
}
