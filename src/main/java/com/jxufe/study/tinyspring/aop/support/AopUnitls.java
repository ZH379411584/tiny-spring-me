package com.jxufe.study.tinyspring.aop.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 15:52
 **/
public class AopUnitls {
    public static Object invokeJoinpoint(Object target, Method method, Object []arguents) throws Throwable {
        try {
            return method.invoke(target,arguents);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());

        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw e.getTargetException();
        }

    }
}
