package com.jxufe.study.tinyspring.beans.factory.support;

import com.jxufe.study.tinyspring.beans.factory.BeanFactory;
import com.jxufe.study.tinyspring.beans.factory.ObjectFactory;
import com.jxufe.study.tinyspring.beans.factory.config.BeanDefinition;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * hong.zheng
 *
 * @Date: 2019-08-26 18:23
 **/
public abstract class DefaultListableBeanFactory implements BeanFactory,BeanDefinitionRegistry{

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(256);

    //本应该在DefaultSingletonBeanRegistry
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>(16);


    public Object getBean(String beanName)  {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(null == beanDefinition){
            throw new IllegalArgumentException("No bean named "+beanName+" is defined");
        }
        Object bean = getSingleton(beanName);
        if(null == bean){
            try {
                 bean = doCreateBean(beanName,beanDefinition);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("create bean exception "+e.getMessage());
            }
        }
        return bean;
    }

    protected abstract Object doCreateBean(String beanName,BeanDefinition beanDefinition) throws Exception;

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        singletonFactories.remove(beanName);
        beanDefinitionMap.put(beanName,beanDefinition);
    }


    protected void addSingletonFactory(String beanName,ObjectFactory<?> objectFactory){
        singletonFactories.put(beanName,objectFactory);
    }

    protected Object getSingleton(String beanName){
        ObjectFactory<?> objectFactory = singletonFactories.get(beanName);
        if(null != objectFactory)
        {
            return objectFactory.getObject();
        }
        return null;
    }
}
