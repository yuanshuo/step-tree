package com.shon.step.core.util;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.shon.step.core.StepNode;

public class JarClassReader {

    public static List<StepNode> read(URL url) {
        if (!"jar".equals(url.getProtocol())) {
            return null;
        }
        List<StepNode> nodes = Lists.newArrayList();
        try {
            JarURLConnection connection = (JarURLConnection)url.openConnection();
            Enumeration<JarEntry> entries = connection.getJarFile().entries();
            String pkgPath = url.getPath().replaceAll("/", ".").substring(url.getPath().indexOf("target/classes") + 15);
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.getName().contains("_") || !entry.getName().startsWith("s")) {
                    continue;
                }
                if (entry.isDirectory()) {
                    StepNode node = new StepNode();
                    node.setCodeType(ElementType.PACKAGE);
                    node.setCodePkgPath(pkgPath + "." + entry.getName());
                    node.setName(entry.getName());
                    nodes.add(node);
                    continue;
                } else {
                    if (!entry.getName().endsWith(".class")) {
                        continue;
                    }
                    String className = entry.getName().substring(0, entry.getName().lastIndexOf("."));
                    String classFullName = pkgPath + "." + className;
                    try {
                        Class<?> clazz = Class.forName(classFullName);
                        StepNode node = new StepNode();
                        node.setCodeType(ElementType.TYPE);
                        node.setCodePkgPath(pkgPath);
                        node.setCodeClazz(clazz);
                        node.setName(entry.getName());
                        nodes.add(node);

                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return nodes;

    }

}
