package com.jxufe.study.tinyspring.aop.framework.adapter;

import com.jxufe.study.tinyspring.aop.Advisor;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 14:48
 **/
public class DefaultAdvisorAdapterRegister implements AdvisorAdapterRegister {
    private final List<AdvisorAdapter> adapters = new ArrayList<AdvisorAdapter>(3);

    public DefaultAdvisorAdapterRegister() {
        //注入方法前
        adapters.add(new MethodBeforeAdviceAdapter());
    }

    public MethodInterceptor[] getInterceptor(Advisor advisor) {
        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        Advice advice = advisor.getAdvice();

        for (AdvisorAdapter advisorAdapter:adapters){
            if(advisorAdapter.supportsAdvice(advice)){
                interceptors.add(advisorAdapter.getInterceptor(advisor));
            }
        }
        return interceptors.toArray(new MethodInterceptor[interceptors.size()]);
    }
}
