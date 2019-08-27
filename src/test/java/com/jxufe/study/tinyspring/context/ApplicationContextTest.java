package com.jxufe.study.tinyspring.context;

import com.jxufe.study.tinyspring.context.support.ClassPathXmlApplicationContext;
import com.jxufe.study.tinyspring.mode.HelloWorld;
import org.junit.Test;

/**
 * hong.zheng
 *
 * @Date: 2019-08-27 20:00
 **/
public class ApplicationContextTest {
    @Test
    public void test() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");

        HelloWorld helloWorld = (HelloWorld) applicationContext.getBean("helloWorld");

        helloWorld.hello();
    }
}
