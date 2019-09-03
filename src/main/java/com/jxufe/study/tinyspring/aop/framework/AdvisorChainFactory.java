package com.jxufe.study.tinyspring.aop.framework;

import java.lang.reflect.Method;
import java.util.List;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 14:03
 **/
public interface AdvisorChainFactory {

    List<Object> getInterceptorsAndDynamicInterceptionAdvice(AdvisedSupport config, Method method, Class<?> targetClass);

}
