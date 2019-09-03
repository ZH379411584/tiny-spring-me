package com.jxufe.study.tinyspring.aop;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 16:41
 **/
public class LoggerBeforeMethodAdive implements MethodBeforeAdvice {
    public void before(Method method, Object[] args, Object target) {
        System.out.println(" name :"+method.getName()+" args :"+ Arrays.toString(args));
    }
}
