package com.shon.step.test;

import com.alibaba.fastjson.JSON;

import com.shon.step.core.StepTree;

public class Test {

    public static void main(String[] args) {
        // 自定义路径扫描实现
        // PackageReflect.reflect("com.shon.step.core.demostep");

        // Reflections工具包
        //Reflections reflections = new Reflections("com.shon.step.domostep");
        //Set<String> allTypes = reflections.get();

        StepTree stepTree = new StepTree("com.shon.step.demostep");
        System.out.println(JSON.toJSONString(stepTree));
        stepTree.execute();
    }
}
