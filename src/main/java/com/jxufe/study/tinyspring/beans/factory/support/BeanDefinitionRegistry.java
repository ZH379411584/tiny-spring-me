package com.jxufe.study.tinyspring.beans.factory.support;

import com.jxufe.study.tinyspring.beans.factory.config.BeanDefinition;

/**
 * hong.zheng
 *
 * @Date: 2019-08-26 18:36
 **/
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
