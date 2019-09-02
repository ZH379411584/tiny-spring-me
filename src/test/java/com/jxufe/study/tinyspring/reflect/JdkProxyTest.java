package com.jxufe.study.tinyspring.reflect;

import com.jxufe.study.tinyspring.mode.IMath;
import com.jxufe.study.tinyspring.mode.Math;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * hong.zheng
 *
 * @Date: 2019-08-31 17:42
 **/
public class JdkProxyTest {
    @Test
    public void test() {
        Math math = new Math();
        //创建一个代理类
        IMath iMath = (IMath) Proxy.newProxyInstance(math.getClass().getClassLoader(),math.getClass().getInterfaces(),new MathInvocationHandler(math));
        System.out.println(iMath.add(1,2));
    }


    private static class MathInvocationHandler implements InvocationHandler {
        private Object target;

        public MathInvocationHandler(Object target) {
            this.target = target;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("before method ");
            Object result = method.invoke(target,args);
            System.out.println("end method and result :"+result);
            return result;
        }
    }





}
