package com.jxufe.study.tinyspring.aop.framework;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 09:07
 **/
public class DefaultAopProxyFactory implements AopProxyFactory {

    public AopProxy createAopProxy(AdvisedSupport config) {
        Class<?> targetClass = config.getTargetClass();
        if(hasNoUserSuppliedProxyInterfaces(config)){
            return new CgLibAopProxy(config);
        }else {
            return new JdkDynamicAopProxy(config);

        }
    }

    private boolean hasNoUserSuppliedProxyInterfaces(AdvisedSupport config){
        Class<?>[] ifcs = config.getProxiedInterfaces();
        return (ifcs.length == 0) ;
    }
}
