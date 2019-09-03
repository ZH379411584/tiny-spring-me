package com.jxufe.study.tinyspring.aop;

import java.lang.reflect.Method;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 15:12
 **/
public interface MethodBeforeAdvice extends BeforeAdvice {

    void before(Method method,Object[] args,Object target);
}
