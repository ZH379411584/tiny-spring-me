package com.jxufe.study.tinyspring.aop.framework;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 08:52
 **/
public interface AopProxyFactory {

    AopProxy createAopProxy(AdvisedSupport config);
}
