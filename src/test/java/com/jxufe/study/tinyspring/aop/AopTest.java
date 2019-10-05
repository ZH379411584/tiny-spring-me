package com.jxufe.study.tinyspring.aop;

import com.jxufe.study.tinyspring.aop.framework.ProxyFactory;
import com.jxufe.study.tinyspring.mode.IMath;
import com.jxufe.study.tinyspring.mode.Math;
import org.junit.Test;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 16:42
 **/
public class AopTest {

    @Test
    public void test() {
        IMath math = new Math();
        math.add(1,2);

        ProxyFactory proxyFactory = new ProxyFactory(IMath.class);
        proxyFactory.setTarget(math);
        proxyFactory.addAdvice(new LoggerBeforeMethodAdive());

        IMath iMath = (IMath) proxyFactory.getProxy();
        iMath.add(1,2);
    }

    @Test
    public void testCglibAop() {
        Math math = new Math();
        math.add(1,2);

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(math);
        proxyFactory.addAdvice(new LoggerBeforeMethodAdive());

        Math mathCglib = (Math) proxyFactory.getProxy();
        mathCglib.add(1,2);
    }
}
