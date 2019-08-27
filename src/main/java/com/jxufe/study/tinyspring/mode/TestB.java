package com.jxufe.study.tinyspring.mode;

/**
 * hong.zheng
 *
 * @Date: 2019-08-27 15:17
 **/
public class TestB {
    private TestA testA;

    public TestA getTestA() {
        return testA;
    }

    public void setTestA(TestA testA) {
        this.testA = testA;
    }

    public void say(){
       testA.say();
    }
}
