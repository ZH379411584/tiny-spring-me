package com.jxufe.study.tinyspring.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * hong.zheng
 *
 * @Date: 2019-08-27 08:59
 **/
public class MutablePropertyValues {
    private final List<PropertyValue> propertyValueList = new ArrayList<PropertyValue>();

    public List<PropertyValue> getPropertyValueList() {
        return propertyValueList;
    }

    public MutablePropertyValues addPropertyValue(PropertyValue propertyValue){
        for (int i = 0; i <propertyValueList.size(); i++) {
            PropertyValue currPv = this.propertyValueList.get(i);
            if(currPv.getName().equals(propertyValue.getName())){
                //TODO merge
            }
        }
        this.propertyValueList.add(propertyValue);
        return this;
    }

    public PropertyValue getPropertyValue(String propertyName){
        for(PropertyValue propertyValue:propertyValueList){
            if(propertyValue.getName().equals(propertyName)){
                return propertyValue;
            }
        }
        return null;
    }

    public PropertyValue[] getPropertyValues(){
        return this.propertyValueList.toArray(new PropertyValue[this.propertyValueList.size()]);
    }
}
