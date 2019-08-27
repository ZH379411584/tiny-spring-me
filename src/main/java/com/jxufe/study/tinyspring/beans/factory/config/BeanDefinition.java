package com.jxufe.study.tinyspring.beans.factory.config;

import com.jxufe.study.tinyspring.beans.MutablePropertyValues;

/**
 * hong.zheng
 *
 * @Date: 2019-08-26 18:31
 **/
public class BeanDefinition {

    private String name;

    private String className;

    private MutablePropertyValues propertyValues;

    public BeanDefinition() {
        propertyValues = new MutablePropertyValues();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public MutablePropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(MutablePropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

}
