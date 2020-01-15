
### spring 源码中相关代码
我们可以猜测spring源码肯定也会先将对象生成，并放置在 某个地方。当出现循环依赖的情况时，会在 某个地方取出引用。


```java
类org.springframework.beans.factory.support.AbstractBeanFactory 

protected <T> T doGetBean(
			final String name, final Class<T> requiredType, final Object[] args, boolean typeCheckOnly)
			throws BeansException {

		final String beanName = transformedBeanName(name);
		Object bean;

		// Eagerly check singleton cache for manually registered singletons.
		Object sharedInstance = getSingleton(beanName);
		
		...
		
```
方法 getSingleton
```java
类 org.springframework.beans.factory.support.DefaultSingletonBeanRegistry

protected Object getSingleton(String beanName, boolean allowEarlyReference) {
	    //singletonObjects Cache of singleton objects: bean name
		Object singletonObject = this.singletonObjects.get(beanName);
		if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
			synchronized (this.singletonObjects) {
               // earlySingletonObjects Cache of early singleton objects: bean name
                // if bean is loading
				singletonObject = this.earlySingletonObjects.get(beanName);
				if (singletonObject == null && allowEarlyReference) {
				    //Cache of singleton factories: bean name
					ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
					if (singletonFactory != null) {
						singletonObject = singletonFactory.getObject();
						this.earlySingletonObjects.put(beanName, singletonObject);
						this.singletonFactories.remove(beanName);
					}
				}
			}
		}
		return (singletonObject != NULL_OBJECT ? singletonObject : null);
	}
```
从方法getSingleton可以看到有三个Map，三个Map的定义如下
```java
	/** Cache of singleton objects: bean name --> bean instance */
	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(256);

	/** Cache of singleton factories: bean name --> ObjectFactory */
	private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>(16);

	/** Cache of early singleton objects: bean name --> bean instance */
	private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);
```
方法getSingleton 调用逻辑如下
1. 先从singletonObjects中获取。
2. 如果没有而且Bean正在创建，则从earlySingletonObjects中获取。
3. 如果还没有，则从singletonFactories中获取，如果存在，则调用ObjectFactory.getObject方法获得该Bean，并将其加入earlySingletonObjects中，同时删除singletonFactories中的元素。

我们可以把singletonObjects，singletonFactories，earlySingletonObjects看作三条流水线。
第一条流水线是singletonFactories，第二条流水线是earlySingletonObjects，第三条流水线是singletonObjects。

方法getSingleton就是先从最后一条也就是第三条流水线获取，如果第三条没有，则从第二条获取，第二条没有，则从第一条获取。第一条有的话，取出来放在第二条流水线中。那么肯定还有第添加到第一条流水线，从第二条流水线取出来放在第三条流水线的代码。


#### singletonFactories 添加Bean实例
```java
类 org.springframework.beans.factory.support.DefaultSingletonBeanRegistry 

protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
		Assert.notNull(singletonFactory, "Singleton factory must not be null");
		synchronized (this.singletonObjects) {
			if (!this.singletonObjects.containsKey(beanName)) {
				this.singletonFactories.put(beanName, singletonFactory);
				//移除第二条流水线
				this.earlySingletonObjects.remove(beanName);
				this.registeredSingletons.add(beanName);
			}
		}
	}

```

#### singletonObjects 添加Bean实例
```java
 类 org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
	protected void addSingleton(String beanName, Object singletonObject) {
		synchronized (this.singletonObjects) {
		    //第三条流水线添加
			this.singletonObjects.put(beanName, (singletonObject != null ? singletonObject : NULL_OBJECT));
			//第一条流水线移除
			this.singletonFactories.remove(beanName);
			//第二条流水线移除
			this.earlySingletonObjects.remove(beanName);
			this.registeredSingletons.add(beanName);
		}
	}
```
现在有两个相循环依赖的Bean，相关类代码和xml配置如下。
```
public class A {
    private B b;

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }
}
public class B {
    private A a;

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }
}
  <bean id="a" class="com.daxiyan.study.spring.model.A">
        <property name="b" ref="b"/>
    </bean>
    <bean id="b" class="com.daxiyan.study.spring.model.B">
        <property name="a" ref="a"/>
    </bean>
    
    
     @Test
        public void test() throws Exception {
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-circular.xml");
            A a = (A) applicationContext.getBean("a");
    
        }

```
调用 getSingleton，addSingletonFactory，addSingleton 的顺序以及调用 方法后 三条流水线中的元素 如下表。注意：
省略了getSingleton 条件 (singletonObject == null && isSingletonCurrentlyInCreation(beanName) 不成立时的情况

action | singletonFactories  | earlySingletonObjects | singletonObjects
---|---|---|---
addSingletonFactory(A)  | A | null | null
addSingletonFactory(B)  | AB | null | null
getSingleton(A) | B | A | null
getSingleton(B) | B | A | null  （allowEarlyReference为false）
addSingleton(B) | null | A | B
getSingleton(A) | B | A  （earlySingletonObjects存在直接返回）| null 
addSingleton(A) | null | null | AB