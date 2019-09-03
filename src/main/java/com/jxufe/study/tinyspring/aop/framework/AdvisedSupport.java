package com.jxufe.study.tinyspring.aop.framework;

import com.jxufe.study.tinyspring.aop.Advisor;
import com.jxufe.study.tinyspring.aop.support.DefaultPointcutAdvisor;
import com.jxufe.study.tinyspring.aop.target.SingletonTargetSource;
import com.jxufe.study.tinyspring.aop.target.TargetSource;
import org.aopalliance.aop.Advice;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 08:54
 **/
public class AdvisedSupport {

    List<Advisor> advisors = new LinkedList<Advisor>();

    TargetSource targetSource ;

    AdvisorChainFactory advisorChainFactory = new DefaultAdvisorChainFactory();

    private List<Class<?>> interfaces = new ArrayList<Class<?>>();

    public TargetSource getTargetSource() {
        return targetSource;
    }

    public Class<?> getTargetClass(){
        return targetSource.getTagetClass();
    }


    public void setTarget(Object target){
        this.targetSource = new SingletonTargetSource(target);
    }

    public void addAdvice(Advice advice){
        advisors.add(new DefaultPointcutAdvisor(advice));
    }

    public List<Advisor> getAdvisors() {
        return advisors;
    }


    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method,Class<?> targetClass){
        return advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(this,method,targetClass);
    }
    public void setInterfaces(Class<?>... interfaces){
        this.interfaces.clear();
        for (Class<?> ifc:interfaces){
            addInterface(ifc);
        }
    }
    public void addInterface(Class<?> intf){
        if(!intf.isInterface()){
            throw new IllegalArgumentException("Interfaces must not be null");
        }
        if(!this.interfaces.contains(intf)){
            this.interfaces.add(intf);
        }
    }
    public Class<?>[] getProxiedInterfaces(){
        return this.interfaces.toArray(new Class<?>[this.interfaces.size()]);
    }
}
