package com.jxufe.study.tinyspring.aop.framework;

import com.jxufe.study.tinyspring.aop.support.AopUnitls;
import com.jxufe.study.tinyspring.aop.target.TargetSource;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 09:09
 **/
public class CgLibAopProxy implements AopProxy {
    private AdvisedSupport advisedSupport;

    public CgLibAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy() {
        TargetSource targetSource = advisedSupport.targetSource;
        Class<?> targetClass = targetSource.getTagetClass();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        Callback[] callbacks = getCallbacks(targetClass);
        enhancer.setCallbacks(callbacks);

        return enhancer.create();
    }


    private Callback[] getCallbacks(Class<?> rootClass) {
        Callback aopInterceptor = new DynamicAdvisedInterceptor(this.advisedSupport);
        Callback[] mainCallbacks = new Callback[] {
                aopInterceptor};

        return mainCallbacks;
    }
    private static class DynamicAdvisedInterceptor implements MethodInterceptor, Serializable {
        private final AdvisedSupport advised;

        public DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

            MethodInvocation invocation;
            TargetSource targetSource = advised.targetSource;
            Class<?> targetClass = targetSource.getClass();
            Object target = targetSource.getTarget();

            List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

            Object returnVal ;
            if(chain.isEmpty()){
                returnVal = proxy.invoke(target, args);
            }else {
                invocation = new CglibMethodInvocation(proxy,target,method,args,targetClass,chain,proxy);
                returnVal = invocation.proceed();
            }
            return returnVal;
        }
    }

    private static class CglibMethodInvocation extends ReflectiveMethodInvocation {

        private final MethodProxy methodProxy;

        private final boolean publicMethod;

        public CglibMethodInvocation(Object proxy, Object target, Method method, Object[] arguments,
                                     Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers, MethodProxy methodProxy) {

            super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
            this.methodProxy = methodProxy;
            this.publicMethod = Modifier.isPublic(method.getModifiers());
        }

        /**
         * Gives a marginal performance improvement versus using reflection to
         * invoke the target when invoking public methods.
         */
        @Override
        protected Object invokeJoinpoint() throws Throwable {
            if (this.publicMethod) {
                return this.methodProxy.invoke(this.target, this.arguments);
            }
            else {
                return super.invokeJoinpoint();
            }
        }
    }
}
