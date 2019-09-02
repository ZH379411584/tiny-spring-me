#### JDK动态代理
```java
import com.jxufe.study.tinyspring.mode.IMath;
import com.jxufe.study.tinyspring.mode.Math;
import org.junit.Test;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
```
#### CgLib代理
```java
import com.jxufe.study.tinyspring.mode.Math;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;

import java.lang.reflect.Method;

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

```
#### 使用aopalliance JDK动态代理
```java
import com.jxufe.study.tinyspring.mode.IMath;
import com.jxufe.study.tinyspring.mode.Math;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AopAllianceTest {

    @Test
    public void test() {
        Math math = new Math();
        //创建一个代理类
        IMath iMath = (IMath) Proxy.newProxyInstance(math.getClass().getClassLoader(),math.getClass().getInterfaces(),
                new AopAllianceTest.MathInvocationHandler(new LoggerMethodInterceptor(),math));
        System.out.println(iMath.add(1,2));
    }


    private static class MathInvocationHandler implements InvocationHandler {
        private MethodInterceptor methodInterceptor;
        private Object target;

        public MathInvocationHandler(MethodInterceptor methodInterceptor, Object target) {
            this.methodInterceptor = methodInterceptor;
            this.target = target;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("before method ");
            Object result = methodInterceptor.invoke(new MyMethodInvocation(target,method,args));
            System.out.println("end method and result :"+result);
            return result;
        }
    }
    
    private static class MyMethodInvocation implements MethodInvocation{
        private Object target;

        private Method method;

        private Object[] args;

        public MyMethodInvocation(Object target, Method method, Object[] args) {
            this.target = target;
            this.method = method;
            this.args = args;
        }

        public Method getMethod() {
            return method;
        }

        public Object[] getArguments() {
            return args;
        }

        public Object proceed() throws Throwable {
            return method.invoke(target, args);
        }

        public Object getThis() {
            return target;
        }

        public AccessibleObject getStaticPart() {
            return method;
        }
    }
   
    private static class LoggerMethodInterceptor implements MethodInterceptor {

        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("logger start");
            Object result = invocation.proceed();
            System.out.println("logger end");
            return result;
        }
    }
    
}


```
