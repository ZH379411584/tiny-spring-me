package com.jxufe.study.tinyspring.aop.support;

import com.jxufe.study.tinyspring.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

/**
 * hong.zheng
 *
 * @Date: 2019-09-03 09:43
 **/
public class DefaultPointcutAdvisor implements PointcutAdvisor {

    private Advice advice;

    public DefaultPointcutAdvisor(Advice advice) {
        this.advice = advice;
    }

    public Advice getAdvice() {
        return advice;
    }
}
