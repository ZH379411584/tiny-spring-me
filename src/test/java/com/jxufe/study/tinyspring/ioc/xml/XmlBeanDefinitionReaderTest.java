package com.jxufe.study.tinyspring.ioc.xml;

import com.jxufe.study.tinyspring.beans.factory.support.DefaultListableBeanFactory;
import com.jxufe.study.tinyspring.beans.factory.xml.XmlBeanFactory;
import com.jxufe.study.tinyspring.core.io.ClassPathResource;
import org.junit.Test;


/**
 * hong.zheng
 *
 * @Date: 2019-08-26 22:23
 **/
public class XmlBeanDefinitionReaderTest {

    @Test
    public void test() throws Exception {
        DefaultListableBeanFactory defaultListableBeanFactory = new XmlBeanFactory(new ClassPathResource("beans.xml"));

        System.out.println(defaultListableBeanFactory);
    }

}
