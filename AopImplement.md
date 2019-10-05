### 1. JDK动态代理
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
### 2. CgLib代理
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
### 3. 使用AopAlliance JDK动态代理
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
            // AopAlliance MethodInterceptor.invoke(MethodInvocation)
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
从上面三种实现AOP的代码可以观察到 2.CgLib代理 和 3. AopAlliance JDK动态代理 将切面的逻辑独立出来，查看SpringAop源码后，发现 Spring CgLib代理和Jdk动态代理 的最底层源码便是如此。
 
#### 使用 AopAlliance JDK代理 分析
##### 主要类
```java
public interface MethodInterceptor extends Interceptor {
	
    /**
     * Implement this method to perform extra treatments before and
     * after the invocation. Polite implementations would certainly
     * like to invoke {@link Joinpoint#proceed()}.
     *
     * @param invocation the method invocation joinpoint
     * @return the result of the call to {@link
     * Joinpoint#proceed()}, might be intercepted by the
     * interceptor.
     *
     * @throws Throwable if the interceptors or the
     * target-object throws an exception.  */
    Object invoke(MethodInvocation invocation) throws Throwable;
}


```


##### MethodInvocation类图
![MethodInvocation](https://github.com/ZH379411584/tiny-spring-me/blob/master/images/MethodInvocation.png)
```java

public interface MethodInvocation extends Invocation
{

    /**
     * Gets the method being called.
     *
     * <p>This method is a frienly implementation of the {@link
     * Joinpoint#getStaticPart()} method (same result).
     *
     * @return the method being called.
     */
    Method getMethod();

}


public interface Joinpoint {

   Object proceed() throws Throwable;
  //省略

}

```

##### 时序图

![MethodInvocation](https://github.com/ZH379411584/tiny-spring-me/blob/master/images/InvocationHandler_invoke.png)


1. MethodInterceptor.invoke();
2. 执行切面逻辑，比如 在在方法执行前打印日志。
3. MethodInvocation.proceed();调用方法原本代码，获取执行结果。
