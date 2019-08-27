package com.jxufe.study.tinyspring.mode;

/**
 * hong.zheng
 *
 * @Date: 2019-08-27 09:52
 **/
public class HelloWorld {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void hello(){
        System.out.println("hello:"+this.name);
    }


}
