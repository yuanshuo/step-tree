package com.shon.step.core.util;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

import com.shon.step.core.StepNode;
import org.apache.commons.collections.CollectionUtils;

public class PackageScanner {

    /**
     * 扫描当前路径下的文件
     * @param packagePath
     * @param errorCodeProps
     * @return
     */
    public static List<StepNode> scan(String packagePath, Properties errorCodeProps) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(packagePath.replace(".", "/"));
        if (null == url) {
            return null;
        }
        List<StepNode> stepNodes = null;
        if ("file".equals(url.getProtocol())) {
            // target目录
            stepNodes = FileClassReader.read(url, errorCodeProps);
        } else if ("jar".equals(url.getProtocol())) {
            // jar包
            stepNodes = JarClassReader.read(url, errorCodeProps);
        } else {
            throw new RuntimeException("url protocol not support");
        }
        if (CollectionUtils.isEmpty(stepNodes)) {
            return null;
        }
        // stepNodes 名称排序
        Collections.sort(stepNodes, Comparator.comparing(StepNode::getName));

        return stepNodes;

    }

}
