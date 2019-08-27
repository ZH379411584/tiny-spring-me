package com.jxufe.study.tinyspring.beans.factory.support;

import com.jxufe.study.tinyspring.beans.PropertyValue;
import com.jxufe.study.tinyspring.beans.factory.BeanFactory;
import com.jxufe.study.tinyspring.beans.factory.ObjectFactory;
import com.jxufe.study.tinyspring.beans.factory.config.BeanDefinition;
import com.jxufe.study.tinyspring.beans.factory.config.RuntimeBeanReference;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * hong.zheng
 *
 * @Date: 2019-08-26 18:23
 **/
public  class DefaultListableBeanFactory implements BeanFactory,BeanDefinitionRegistry{

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(256);

    //源码中在DefaultSingletonBeanRegistry类中
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


    protected Object doCreateBean(String beanName,BeanDefinition beanDefinition) throws Exception {
        String className = beanDefinition.getClassName();
        //先创建对象，并将对象放入工厂
        final Object bean =  Class.forName(className).newInstance();
        addSingletonFactory(beanName, new ObjectFactory<Object>() {
            public Object getObject() {
                return bean;
            }
        });
        //再处理属性
        applyPropertyValues(bean,beanDefinition);
        return bean;
    }

    public void applyPropertyValues(Object object, BeanDefinition beanDefinition) throws Exception {
        PropertyValue[] propertyValues = beanDefinition.getPropertyValues().getPropertyValues();
        for (PropertyValue propertyValue : propertyValues){
            Object value = propertyValue.getValue();
            if (RuntimeBeanReference.class.isInstance(value)) {
                RuntimeBeanReference beanReference = (RuntimeBeanReference) value;
                value = getBean(beanReference.getBeanName());
            }
            Field field = object.getClass().getDeclaredField(propertyValue.getName());
            field.setAccessible(true);
            field.set(object, value);
        }
    }
}
