package com.shon.step.test.demo;

import java.net.URL;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import com.alibaba.fastjson.JSON;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compilation.Status;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import com.shon.step.core.StepTree;
import com.shon.step.core.anotation.StepErrorCodeProcessor;

public class Test {

    public static void main(String[] args) {
        // 自定义路径扫描实现
        // PackageReflect.reflect("com.shon.step.core.demostep");

        // Reflections工具包
        //Reflections reflections = new Reflections("com.shon.step.domostep");
        //Set<String> allTypes = reflections.get();

        StepTree stepTree = new StepTree("com.shon.step.test.demostep");
        System.out.println(JSON.toJSONString(stepTree));
        stepTree.execute();

    }
}
