package com.jxufe.study.tinyspring.beans;

/**
 * hong.zheng
 *
 * @Date: 2019-08-27 08:57
 **/
public class PropertyValue {

    private final String name;

    private final Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }



}
