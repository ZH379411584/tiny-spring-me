package com.jxufe.study.tinyspring.beans.factory.xml;

import com.jxufe.study.tinyspring.beans.PropertyValue;
import com.jxufe.study.tinyspring.beans.factory.config.BeanDefinition;
import com.jxufe.study.tinyspring.beans.factory.config.RuntimeBeanReference;
import com.jxufe.study.tinyspring.beans.factory.support.BeanDefinitionRegistry;
import com.jxufe.study.tinyspring.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * hong.zheng
 *
 * @Date: 2019-08-26 18:36
 **/
public class XmlBeanDefinitionReader {

    private BeanDefinitionRegistry registry;


    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    public void loadBeanDefinitions(Resource resource) throws Exception {
        InputStream inputStream = resource.getInputStream();
        doloadBeanDefinitions(inputStream);
    }


    public void doloadBeanDefinitions(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(inputStream);
        Element root = doc.getDocumentElement();
        parseBeanDefinitions(root);
        inputStream.close();
    }

    protected void parseBeanDefinitions(Element root) {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                processBeanDefinition(ele);
            }
        }
    }

    /**
     * 解析id，class
     * @param ele
     */
    protected void processBeanDefinition(Element ele) {
        String name = ele.getAttribute("id");
        String className = ele.getAttribute("class");
        BeanDefinition beanDefinition = new BeanDefinition();
        processProperty(ele, beanDefinition);
        beanDefinition.setClassName(className);
        beanDefinition.setName(name);
        getRegistry().registerBeanDefinition(name, beanDefinition);
    }

    /**
     * 解析属性
     * @param root
     * @param beanDefinition
     */
    protected void processProperty(Element root,BeanDefinition beanDefinition){
        NodeList propertyNode = root.getElementsByTagName("property");
        for (int i = 0; i <propertyNode.getLength() ; i++) {
            Node node = propertyNode.item(i);
            if (node instanceof Element) {
                Element propertyEle = (Element) node;
                String name = propertyEle.getAttribute("name");
                String value = propertyEle.getAttribute("value");
                if (value != null && value.length() > 0) {
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name,value));
                }else{//处理对象引用
                    String ref = propertyEle.getAttribute("ref");
                    if(ref == null || ref.length() == 0 ){
                        throw new IllegalArgumentException("Configuation probleam:<property> element for property ' "+name+
                                "' must specify a ref or value");
                    }
                    RuntimeBeanReference beanReference = new RuntimeBeanReference(ref);
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name,beanReference));
                }
            }

        }

    }






}
