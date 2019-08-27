package com.jxufe.study.tinyspring.beans.factory.config;

/**
 * hong.zheng
 *
 * @Date: 2019-08-27 11:41
 **/
public class RuntimeBeanReference {
    private final String beanName;

    private Object source;

    public RuntimeBeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
