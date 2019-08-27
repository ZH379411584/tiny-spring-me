package com.jxufe.study.tinyspring.context.support;


import com.jxufe.study.tinyspring.beans.factory.support.DefaultListableBeanFactory;
import com.jxufe.study.tinyspring.beans.factory.xml.XmlBeanDefinitionReader;
import com.jxufe.study.tinyspring.core.io.ClassPathResource;

/**
 * hong.zheng
 *
 * @Date: 2019-08-27 19:16
 **/
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {
    private String configLocation;

    public ClassPathXmlApplicationContext(String configLocation)  {
        this.configLocation = configLocation;
        refresh();
    }


    protected void loadBeanDefinitions(DefaultListableBeanFactory defaultListableBeanFactory) {
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
        try {
            xmlBeanDefinitionReader.loadBeanDefinitions(new ClassPathResource(configLocation));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("loadBeanDefinitions error "+e.getMessage());
        }

    }
}
