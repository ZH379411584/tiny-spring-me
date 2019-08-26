package com.jxufe.study.tinyspring.beans.factory.xml;

import com.jxufe.study.tinyspring.beans.factory.support.DefaultListableBeanFactory;
import com.jxufe.study.tinyspring.core.io.Resource;

import java.io.IOException;

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

}
