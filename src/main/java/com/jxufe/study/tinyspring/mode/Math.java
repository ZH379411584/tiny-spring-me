package com.jxufe.study.tinyspring.mode;

/**
 * hong.zheng
 *
 * @Date: 2019-08-26 22:35
 **/
public class Math implements IMath{
    public Math() {
    }

    public int add(Integer a, Integer b) {
        return a+b;
    }

    public void createIMath(){
        final int c = 0;
        IMath iMath = new IMath() {
            public int add(Integer a, Integer b) {
                return c;
            }
        };
    }


}
