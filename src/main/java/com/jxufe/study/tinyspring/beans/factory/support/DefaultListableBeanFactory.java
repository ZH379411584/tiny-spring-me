package com.jxufe.study.tinyspring.beans.factory.support;

import com.jxufe.study.tinyspring.beans.factory.BeanFactory;
import com.jxufe.study.tinyspring.beans.factory.config.BeanDefinition;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * hong.zheng
 *
 * @Date: 2019-08-26 18:23
 **/
public class DefaultListableBeanFactory implements BeanFactory,BeanDefinitionRegistry{

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(256);

    public Object getBean(String name) {
        return null;
    }

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName,beanDefinition);
    }
}
