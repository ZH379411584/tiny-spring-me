package com.jxufe.study.tinyspring.aop.framework;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 08:51
 **/
public class ProxyFactory extends AdvisedSupport{

    private AopProxyFactory aopProxyFactory;

    public ProxyFactory(Class<?> ... proxyInterfaces) {
        aopProxyFactory = new DefaultAopProxyFactory();
        setInterfaces(proxyInterfaces);
    }

    public Object getProxy(){
        return aopProxyFactory.createAopProxy(this).getProxy();
    }

}
