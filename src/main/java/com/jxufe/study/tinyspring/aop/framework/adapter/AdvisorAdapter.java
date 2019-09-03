package com.jxufe.study.tinyspring.aop.framework.adapter;

import com.jxufe.study.tinyspring.aop.Advisor;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 14:59
 **/
public interface AdvisorAdapter {

    boolean supportsAdvice(Advice advice);

    MethodInterceptor getInterceptor(Advisor advisor);

}
