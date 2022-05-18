package com.shon.step.core.util;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.shon.step.core.StepNode;
import javafx.scene.Parent;
import org.apache.commons.lang3.StringUtils;

public class FileClassReader {

    /**
     * 当前目录的节点
     * @param url
     * @param errorCodeProps
     * @return
     */
    public static List<StepNode> read(URL url, Properties errorCodeProps) {
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

                    HashSet<String> codeSet = Sets.newHashSet();
                    String errors = errorCodeProps.getProperty(clazz.getName());
                    if (StringUtils.isNotBlank(errors)) {
                        codeSet.addAll(Lists.newArrayList(errors.split(",")));
                    }

                    // 1层父类
                    Class<?> parent = clazz.getSuperclass();
                    String parentErrors = errorCodeProps.getProperty(parent.getName());
                    if (StringUtils.isNotBlank(parentErrors)) {
                        codeSet.addAll(Lists.newArrayList(parentErrors.split(",")));
                    }
                    // 1层组合类
                    Field[] declaredFields = clazz.getDeclaredFields();
                    for (Field declaredField : declaredFields) {
                        Class<?> declaringClass = declaredField.getType();
                        String fieldErrors = errorCodeProps.getProperty(declaringClass.getName());
                        if (StringUtils.isNotBlank(fieldErrors)) {
                            codeSet.addAll(Lists.newArrayList(fieldErrors.split(",")));
                        }
                    }
                    StepNode node = new StepNode();
                    node.setCodeType(ElementType.TYPE);
                    node.setCodePkgPath(pkgPath);
                    node.setCodeClazz(clazz);
                    node.setName(file.getName());
                    node.setErrorCodes(codeSet);
                    nodes.add(node);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return nodes;
    }
}
