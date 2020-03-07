

## IOC概念
IOC:Inversion of Control （控制反转）
它将类与类之间的依赖从代码中脱离出来，用配置的方式进行依赖关系的描述，由ioc容器负责依赖类之间的创建，拼接，管理，获取等工作。底层使用反射创建类并设置属性。

## IOC流程
```java

// 1.解析xml
BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("beans.xml"));
// 2.生成Bean
HelloWorld helloWorld = (HelloWorld) beanFactory.getBean("helloWorldService");
// 3.调用
helloWorld.helloWorld();

```

### 1.解析xml

#### 问题1：如何解析xml，解析的结果是？ 
- xml 解析代码
```java
public void doloadBeanDefinitions(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(inputStream);
        Element root = doc.getDocumentElement();
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                String name = ele.getAttribute("id");
                String className = ele.getAttribute("class");
            }
        }
       
    }
```
- 解析的结果应该是 反射创建类实例需要的类名，类属性...  


#### 问题2：解析完成的数据存在哪里？
解析的结果放在BeanDefinition中，DefaultListableBeanFactory的beanDefinitionMap存储解析的BeanDefinition。


### 2.生成Bean
#### 问题1：如何从解析的数据中生成Bean？
根据BeanDefinition中的类名，类属性 反射生成。

#### 问题2：如何解决循环依赖？
```
A与B互相依赖的基本过程是这样。
A a = new A();
B b = new B();
a.b = b;
b.a = a;
```

spring不支持构造器循环依赖，prototype范围的循环依赖，只支持setter循环依赖。    
tiny-spring-me的解决方案
1. 实例化对象
2. 将实例化对象加入一个singletonFactories中。
3. 再处理对象的每个属性，如果属性是某个对象，则先在singletonFactories中寻找，如果存在，则使用singletonFactories保存的对象。如果没有，则创建该Bean。
```java
tiny-spring-me中 DefaultListableBeanFactory类

private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>(16);

 protected Object doCreateBean(String beanName,BeanDefinition beanDefinition) throws Exception {
        String className = beanDefinition.getClassName();
       
        final Object bean =  Class.forName(className).newInstance();
        //先创建对象，并将对象放入工厂
        addSingletonFactory(beanName, new ObjectFactory<Object>() {
            public Object getObject() {
                return bean;
            }
        });
        //再处理属性
        applyPropertyValues(bean,beanDefinition);
        return bean;
    }
   protected void addSingletonFactory(String beanName,ObjectFactory<?> objectFactory){
        singletonFactories.put(beanName,objectFactory);
    }
    
   public Object getBean(String beanName)  {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(null == beanDefinition){
            throw new IllegalArgumentException("No bean named "+beanName+" is defined");
        }
        //从singletonFactories中查找
        Object bean = getSingleton(beanName);
        if(null == bean){
            try {
                 bean = doCreateBean(beanName,beanDefinition);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("create bean exception "+e.getMessage());
            }
        }
        return bean;
   }
   protected Object getSingleton(String beanName){
        ObjectFactory<?> objectFactory = singletonFactories.get(beanName);
        if(null != objectFactory)
        {
            return objectFactory.getObject();
        }
        return null;
    }

```

#### Spring 源码处理循环依赖
[Spring 源码处理循环依赖](https://github.com/ZH379411584/tiny-spring-me/blob/master/ResolveCircularReferences.md)


## 相关理论
### XmlBeanFactory 类继承图
![XmlBeanFactory](https://github.com/ZH379411584/tiny-spring-me/blob/master/images/XmlBeanFactory_uml.png)





