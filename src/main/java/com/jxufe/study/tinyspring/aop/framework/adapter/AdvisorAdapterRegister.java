package com.jxufe.study.tinyspring.aop.framework.adapter;

import com.jxufe.study.tinyspring.aop.Advisor;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 14:46
 **/
public interface AdvisorAdapterRegister {

    MethodInterceptor[] getInterceptor(Advisor advisor);
}
