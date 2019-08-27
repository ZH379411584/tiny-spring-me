package com.jxufe.study.tinyspring.mode;

/**
 * hong.zheng
 *
 * @Date: 2019-08-27 15:17
 **/
public class TestA {

    private TestB testB;

    public TestB getTestB() {
        return testB;
    }

    public void setTestB(TestB testB) {
        this.testB = testB;
    }

    public void say(){
        System.out.println("testA");
    }
}
