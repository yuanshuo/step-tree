package com.shon.step.test.demo;

import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

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

        try {

            //ResourceBundle resource = ResourceBundle.getBundle("generated-sources/annotations/step-error-code.properties");
            //ResourceBundle resource = ResourceBundle.getBundle("resource.properties");
            //FileReader fileReader = new FileReader("step-error-code.properties");

            //properties.load(fileReader);
            //URL resource = Test.class.getResource("resource.properties");
            //System.out.println(resource.getPath());

            Properties properties = new Properties();
            InputStream inputStream = Test.class.getClassLoader().getResourceAsStream("step-error-code.properties");
            properties.load(inputStream);

            String res = properties.getProperty("com.shon.step.test.demostep.s0_SelectOne");
            System.out.println("resource:" + res);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
