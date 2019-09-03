package com.jxufe.study.tinyspring.aop.framework;

import com.jxufe.study.tinyspring.aop.Advisor;
import com.jxufe.study.tinyspring.aop.PointcutAdvisor;
import com.jxufe.study.tinyspring.aop.framework.adapter.AdvisorAdapterRegister;
import com.jxufe.study.tinyspring.aop.framework.adapter.DefaultAdvisorAdapterRegister;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 14:08
 **/
public class DefaultAdvisorChainFactory implements AdvisorChainFactory {

    private AdvisorAdapterRegister advisorAdapterRegister = new DefaultAdvisorAdapterRegister();

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(AdvisedSupport config, Method method, Class<?> targetClass) {
        List<Object> interceptor =  new ArrayList<Object>(config.getAdvisors().size());
        Class<?> actualClss = (targetClass != null ? targetClass : method.getDeclaringClass());

        for (Advisor advisor:config.getAdvisors()) {
            //
            if(advisor instanceof PointcutAdvisor){
                //classFilter 和 methodMatch
                MethodInterceptor[] methodInterceptors = advisorAdapterRegister.getInterceptor(advisor);
                interceptor.addAll(Arrays.asList(methodInterceptors));
            }

        }
        return interceptor;
    }
}
