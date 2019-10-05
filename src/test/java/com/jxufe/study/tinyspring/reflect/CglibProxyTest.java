package com.jxufe.study.tinyspring.reflect;

import com.jxufe.study.tinyspring.mode.Math;
import net.sf.cglib.proxy.*;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * hong.zheng
 *
 * @Date: 2019-08-31 18:13
 **/
public class CglibProxyTest {

    @Test
    public void test() {
        MathMethodInterceptor mathMethodInterceptor = new MathMethodInterceptor();

        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(mathMethodInterceptor);
        enhancer.setSuperclass(Math.class);
        Math mathProxy = (Math) enhancer.create();
        System.out.println(mathProxy.add(1,2));
    }

    private static class MathMethodInterceptor implements MethodInterceptor {

        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            System.out.println("before method ");
            Object result = methodProxy.invokeSuper(o,objects);
            System.out.println("end method and result :"+result);
            return result;
        }
    }
}
