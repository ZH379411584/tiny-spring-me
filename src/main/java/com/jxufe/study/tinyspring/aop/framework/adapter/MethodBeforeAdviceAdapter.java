package com.jxufe.study.tinyspring.aop.framework.adapter;

import com.jxufe.study.tinyspring.aop.Advisor;
import com.jxufe.study.tinyspring.aop.MethodBeforeAdvice;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 15:02
 **/
public class MethodBeforeAdviceAdapter implements AdvisorAdapter{

    public boolean supportsAdvice(Advice advice) {
        return advice instanceof MethodBeforeAdvice;
    }

    public MethodInterceptor getInterceptor(Advisor advisor) {
        MethodBeforeAdvice methodBeforeAdvice = (MethodBeforeAdvice) advisor.getAdvice();
        return new MethodBeforeAdviceInterceptor(methodBeforeAdvice);
    }
}
