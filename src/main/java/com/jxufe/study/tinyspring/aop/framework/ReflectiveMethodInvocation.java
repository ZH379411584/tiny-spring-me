package com.jxufe.study.tinyspring.aop.framework;

import com.jxufe.study.tinyspring.aop.support.AopUnitls;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 15:26
 **/
public class ReflectiveMethodInvocation implements MethodInvocation {

    protected final Object proxy;
    protected final Object target;
    protected final Method method;
    protected Object []arguents;
    private final Class<?> targetClass;
    protected final List<?> interceptorsAndDynamicMethodMatchers;
    private int currentInterceptorIndex = -1;

    public ReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguents, Class<?> targetClass, List<?> interceptorsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguents = arguents;
        this.targetClass = targetClass;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    public Method getMethod() {
        return this.method;
    }

    public Object[] getArguments() {
        return (this.arguents != null ? this.arguents : new Object[0]);
    }

    public Object proceed() throws Throwable {
        if(this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() -1 ){
            return AopUnitls.invokeJoinpoint(this.target,this.method,this.arguents);
        }
        Object interceptor = this.interceptorsAndDynamicMethodMatchers.get(++currentInterceptorIndex);
        return ((MethodInterceptor)(interceptor)).invoke(this);
    }

    public Object getThis() {
        return this.target;
    }

    public AccessibleObject getStaticPart() {
        return null;
    }

    public Object[] getArguents() {
        return arguents;
    }

    public void setArguents(Object[] arguents) {
        this.arguents = arguents;
    }


}
