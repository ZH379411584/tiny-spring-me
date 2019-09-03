package com.jxufe.study.tinyspring.aop.framework;

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
        return null;
    }
}
