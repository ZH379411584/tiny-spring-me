## AOP概念
面向切面编程。
### AOP概念
- 连接点  
程序执行的某个特定位置：如类开始初始化前，类初始化后，类某个方法调用前，调用后，方法抛出异常后。一个类或一段程序代码拥有一些具有边界性质的特定点，这些代码中的特定点就叫做连接点。

- 切点  
每个程序类有多个连接点，如一个拥有两个方法的类，这两个方法都是连接点，即连接点是程序类中客观存在的事物。但在这为数众多的连接点中，如何定位到某个感兴趣的连接点上？Aop通过“切点”定位特定连接点。连接点相当于数据库多条记录，切点相当于查询条件。但确切的说，不能称之为查询连接点，因为连接点是方法执行前、执行后等包括方位信息的具体程序执行点，而切点只定位到某个方法上，所以如果希望定位到具体的连接点上，还需要提供方位信息。

- 增强（Advice）  
增强是织入到目标类连接点的一段程序代码。Spring提供的增强接口都是带方位名的，BeforeAdvice，AfterReturnAdvice，ThrowsAdvice等。BeforeAdvice表示方法调用前的位置，所以只有结合切点和增强两者才能确保特定的连接点并实施增加逻辑。

- 目标对象  
增强逻辑的织入目标类，如果没有AOP，目标业务类需要自己实现所有逻辑。

- 引介（Introduction）  
引介是一种特殊的增强，它为类添加一些属性和方法。这样，即使一个业务类原来没有实现某个接口，通过AOP的引介功能，我们可以动态地为该业务添加接口实现逻辑，让业务类成为这个接口的实现类。

- 织入（Weaving）  
织入是将增强添加到目标类具体连接点上的过程，AOP像一条织布机，将目标类、增强或者引介通过AOP这台织布机天衣无缝地编织到一起。根据不同的实现技术,AOP有三种织入的方式。
1. 编译期织入,这要求使用特殊的Java编译器。
2. 类装载器织入，这要求使用特殊的类装载器。
3. 动态代理织入，在运行期为目标类添加增加生成子类的方式。

### 如何实现AOP
#### aspectJ
AspectJ是语言级的AOP的实现，AspectJ扩展了Java语言，定义了AOP语法，能够在编译期提供横切代码的织入，所以它有一个专门的编译器来生成遵守java字节编码规范的Class文件。
#### SpringAop
使用纯Java实现，它不需要专门的编译过程，不需要特殊的类装载器，它在运行期通过代理方式向目标类织入增强代码。
Spring并不尝试提供最完整的AOP实现，相反，它侧重于提供一种和Spring Ioc容器整合的AOP实现，用以解决企业级开发中常见的问题。
在Spring中，我们可以无缝的将Spring AOP ，IOC和AspectJ整合在一起。


## SpringAOP
AOP使用例子  
XML配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">
    <!-- 开启AOP注解-->
    <aop:aspectj-autoproxy/>
    <!-- 目标对象 -->
    <bean id="waiter" class="com.daxiyan.study.spring.aop.NaiveWaiter"/>
    <!-- 切面 -->
    <bean id="greetingAspect" class="com.daxiyan.study.spring.aspect.GreetingAspect"/>

</beans>
```
目标对象 NaiveWaiter
```java

public class NaiveWaiter implements Waiter {
    @Override
    public void greetTo(String name) {
        System.out.println("greet to "+ name+" ... ");
    }

    @Override
    public void serveTo(String name) {
        System.out.println("serving to "+name+"... ");
    }
}

```
切面 GreetingAspect
```java
 @Aspect
 public class GreetingAspect {
     @Before("execution(* greetTo(..))")
     public void beforeGreeting(JoinPoint joinPoint)
     {
         System.out.println("how are you "+ Arrays.toString(joinPoint.getArgs()));
     }
 }

```
这些简单的配置就实现了AOP功能，暂时认为做了两件事情。
1. 解析了切面GreetingAspect中的增强（Advice），切点表达式。
2. 为目标对象NaiveWaiter生成了代理，在生成代理的时候需要切点表达式匹配。


## SpringAop要解决的问题 

### 如何生成代理？
#### [JDK动态代理，Cglib代理实现](https://github.com/ZH379411584/tiny-spring-me/blob/master/AopImplement.md)

tiny-spring-me 暂时不考虑解析切面，使用编码传入增强（Advice）。
```java 
Waiter waiter = new NaiveWaiter();
//前置增强
BeforeAdvice beforeAdvice = new GreetingBeforeAdvice();
//
AfterReturningAdvice afterReturningAdvice = new GreetingAfterAdvice();
ProxyFactory proxyFactory = new ProxyFactory();
//设置织入目标
proxyFactory.setTarget(waiter);
proxyFactory.addAdvice(beforeAdvice);
proxyFactory.addAdvice(afterReturningAdvice);
waiter = (Waiter) proxyFactory.getProxy();
waiter.greetTo("mindong");
```
### 什么时候生成代理类？
在Bean的生命周期时，如果BeanFactory装配了BeanPostProcessor的话，在Bean初始化的过程中，会对对进行加工操作。spring框架中org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator 这个类就是实现AOP代理功能的。

```
@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		Object cacheKey = getCacheKey(beanClass, beanName);

		if (beanName == null || !this.targetSourcedBeans.contains(beanName)) {
			if (this.advisedBeans.containsKey(cacheKey)) {
				return null;
			}
			if (isInfrastructureClass(beanClass) || shouldSkip(beanClass, beanName)) {
				this.advisedBeans.put(cacheKey, Boolean.FALSE);
				return null;
			}
		}

		// Create proxy here if we have a custom TargetSource.
		// Suppresses unnecessary default instantiation of the target bean:
		// The TargetSource will handle target instances in a custom fashion.
		if (beanName != null) {
			TargetSource targetSource = getCustomTargetSource(beanClass, beanName);
			if (targetSource != null) {
				this.targetSourcedBeans.add(beanName);
				Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(beanClass, beanName, targetSource);
				Object proxy = createProxy(beanClass, beanName, specificInterceptors, targetSource);
				this.proxyTypes.put(cacheKey, proxy.getClass());
				return proxy;
			}
		}

		return null;
	}
```
### 如何保证多种Advice的调用顺序？
排序代码
```
类org.springframework.aop.aspectj.annotation.ReflectiveAspectJAdvisorFactory

private static final Comparator<Method> METHOD_COMPARATOR;

	static {
		CompoundComparator<Method> comparator = new CompoundComparator<Method>();
		comparator.addComparator(new ConvertingComparator<Method, Annotation>(
				new InstanceComparator<Annotation>(
						Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class),
				new Converter<Method, Annotation>() {
					@Override
					public Annotation convert(Method method) {
						AspectJAnnotation<?> annotation =
								AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(method);
						return (annotation != null ? annotation.getAnnotation() : null);
					}
				}));
		comparator.addComparator(new ConvertingComparator<Method, String>(
				new Converter<Method, String>() {
					@Override
					public String convert(Method method) {
						return method.getName();
					}
				}));
		METHOD_COMPARATOR = comparator;
	}
       
private List<Method> getAdvisorMethods(Class<?> aspectClass) {
		final List<Method> methods = new LinkedList<Method>();
		ReflectionUtils.doWithMethods(aspectClass, new ReflectionUtils.MethodCallback() {
			@Override
			public void doWith(Method method) throws IllegalArgumentException {
				// Exclude pointcuts
				if (AnnotationUtils.getAnnotation(method, Pointcut.class) == null) {
					methods.add(method);
				}
			}
		});
		Collections.sort(methods, METHOD_COMPARATOR);
		return methods;
	}
```
