```java

// 1.解析xml
BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("beans.xml"));
// 2.生成Bean
HelloWorld helloWorld = (HelloWorld) beanFactory.getBean("helloWorldService");
// 3.调用
helloWorld.helloWorld();

```

## IOC概念
IOC:Inversion of Control （控制反转）
它将类与类之间的依赖从代码中脱离出来，用配置的方式进行依赖关系的描述，由ioc容器负责依赖类之间的创建，拼接，管理，获取等工作。底层使用反射创建类并设置属性。

## IOC流程

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
- 解析的结果应该是 反射创建类实例需要的类名，类属性...  解析的结果放在BeanDefinition中，


#### 问题2：解析完成的数据存在哪里？

#### 联想


### 2.生成Bean
#### 问题1：如何从解析的数据中生成Bean？
#### 问题2：如何解决循环依赖？


#### 联想
## 相关理论
### BeanFactory 类继承图

##


