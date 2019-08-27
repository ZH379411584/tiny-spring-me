package com.jxufe.study.tinyspring.context.support;

import com.jxufe.study.tinyspring.beans.factory.support.DefaultListableBeanFactory;
import com.jxufe.study.tinyspring.context.ApplicationContext;

/**
 * hong.zheng
 *
 * @Date: 2019-08-27 19:21
 **/
public abstract class AbstractApplicationContext implements ApplicationContext {

    protected DefaultListableBeanFactory defaultListableBeanFactory;


    public AbstractApplicationContext() {
    }

    public Object getBean(String beanName) {
        return defaultListableBeanFactory.getBean(beanName);
    }

    public void refresh(){
        //
        this.defaultListableBeanFactory = obtainFreshBeanFactory();
    }


    protected DefaultListableBeanFactory obtainFreshBeanFactory()
    {
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
        loadBeanDefinitions(defaultListableBeanFactory);
        return defaultListableBeanFactory;
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory defaultListableBeanFactory);
}
