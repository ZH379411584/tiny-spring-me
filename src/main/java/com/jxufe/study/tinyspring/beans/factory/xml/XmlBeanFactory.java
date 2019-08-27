package com.jxufe.study.tinyspring.beans.factory.xml;

import com.jxufe.study.tinyspring.beans.PropertyValue;
import com.jxufe.study.tinyspring.beans.factory.ObjectFactory;
import com.jxufe.study.tinyspring.beans.factory.config.BeanDefinition;
import com.jxufe.study.tinyspring.beans.factory.config.RuntimeBeanReference;
import com.jxufe.study.tinyspring.beans.factory.support.DefaultListableBeanFactory;
import com.jxufe.study.tinyspring.core.io.Resource;

import java.lang.reflect.Field;

/**
 * hong.zheng
 *
 * @Date: 2019-08-26 18:18
 **/
public class XmlBeanFactory extends DefaultListableBeanFactory {


    private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this);

    public XmlBeanFactory(Resource resource) throws Exception {
        reader.loadBeanDefinitions(resource);
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
