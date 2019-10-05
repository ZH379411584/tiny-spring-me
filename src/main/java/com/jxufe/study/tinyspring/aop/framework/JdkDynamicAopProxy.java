package com.jxufe.study.tinyspring.aop.framework;

import com.jxufe.study.tinyspring.aop.support.AopUnitls;
import com.jxufe.study.tinyspring.aop.target.TargetSource;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 09:08
 **/
public class JdkDynamicAopProxy  implements AopProxy, InvocationHandler {

    private AdvisedSupport advisedSupport;

    public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), advisedSupport.getTargetClass().getInterfaces(), this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodInvocation invocation;
        TargetSource targetSource = advisedSupport.targetSource;
        Class<?> targetClass = targetSource.getClass();
        Object target = targetSource.getTarget();

        List<Object> chain = this.advisedSupport.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

        Object returnVal ;
        if(chain.isEmpty()){
            returnVal = AopUnitls.invokeJoinpointUsingReflection(target,method,args);
        }else {
            invocation = new ReflectiveMethodInvocation(proxy,target,method,args,targetClass,chain);
            returnVal = invocation.proceed();
        }
        return returnVal;
    }
}
