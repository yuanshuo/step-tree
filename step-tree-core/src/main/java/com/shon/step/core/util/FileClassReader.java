package com.shon.step.core.util;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

import com.google.common.collect.Lists;
import com.shon.step.core.StepNode;

public class FileClassReader {

    /**
     * 当前目录的节点
     * @param url
     * @return
     */
    public static List<StepNode> read(URL url) {
        File[] files = new File(url.getPath().replaceAll("%20", " ")).listFiles();
        if (files == null) {
            return null;
        }
        List<StepNode> nodes = Lists.newArrayList();
        String pkgPath = url.getPath().replaceAll("/", ".").substring(url.getPath().indexOf("target/classes") + 15);
        for (File file : files) {
            if (!file.getName().contains("_") || !file.getName().startsWith("s")) {
                continue;
            }
            if (file.isDirectory()) {
                StepNode node = new StepNode();
                node.setCodeType(ElementType.PACKAGE);
                node.setCodePkgPath(pkgPath + "." + file.getName());
                node.setName(file.getName());
                nodes.add(node);
                continue;
            }
            if(file.isFile()) {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = file.getName().substring(0, file.getName().lastIndexOf("."));
                String classFullName = pkgPath + "." + className;
                try {
                    Class<?> clazz = Class.forName(classFullName);
                    StepNode node = new StepNode();
                    // 反射获取方法
                    try {
                        Method method = clazz.getMethod("getNameTest");
                        if (null != method) {
                            Object obj = clazz.newInstance();
                            Object res = method.invoke(obj);
                            node.setErrorCodes(res);
                        }
                    } catch (Exception e) {

                    }


                    node.setCodeType(ElementType.TYPE);
                    node.setCodePkgPath(pkgPath);
                    node.setCodeClazz(clazz);
                    node.setName(file.getName());
                    nodes.add(node);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return nodes;
    }
}
