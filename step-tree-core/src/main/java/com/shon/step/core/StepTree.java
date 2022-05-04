package com.shon.step.core;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.shon.step.core.anotation.Step;
import com.shon.step.core.util.PackageScanner;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

/**
 * 步骤执行树
 */
@Getter
@Setter
public class StepTree {
    /**
     * 根节点
     */
    private StepNode root;

    public StepTree(String packagePath) {
        // 构造步骤树
        root = new StepNode();
        root.setName(packagePath.substring(packagePath.lastIndexOf(".") + 1));
        root.setCodePkgPath(packagePath);
        root.setCodeType(ElementType.PACKAGE);

        _reflectTree(root);
        _reflectStepDesc(Lists.newArrayList(root));
    }

    /**
     * 递归构造步骤树
     * @param root
     */
    private void _reflectTree(StepNode root) {
        if (!ElementType.PACKAGE.equals(root.getCodeType())) {
            return;
        }
        List<StepNode> childs = PackageScanner.scan(root.getCodePkgPath());
        if (CollectionUtils.isEmpty(childs)) {
            return;
        }
        root.setChilds(childs);

        List<StepNode> pkgNodes = childs.stream().filter(node -> {
            return ElementType.PACKAGE.equals(node.getCodeType());
        }).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(pkgNodes)) {
            _reflectStepDesc(childs);
            return;
        }

        for (StepNode pkgNode : pkgNodes) {
            // 递归查找子节点
            _reflectTree(pkgNode);
        }

        // 采集到叶子开始回溯获取描述
        // 在开始没有类加载的时候，package的信息classLoader也不会加载
        _reflectStepDesc(childs);


    }

    /**
     * 获取步骤描述
     * @param childs
     */
    private void _reflectStepDesc(List<StepNode> childs) {
        Step annotation = null;
        for (StepNode child : childs) {
            switch (child.getCodeType()) {
                case TYPE:
                    annotation = child.getCodeClazz().getAnnotation(Step.class);
                    child.setDesc(null == annotation ? "" : annotation.desc());
                    break;
                case PACKAGE:
                    annotation = Package.getPackage(child.getCodePkgPath()).getAnnotation(Step.class);
                    child.setDesc(null == annotation ? "" : annotation.desc());
                    break;
                default:
                    child.setDesc("");

            }
        }
    }

    /**
     * 递归执行步骤树
     */
    public void execute() {
        _execute(root);
    }

    private void _execute(StepNode root) {
        if (CollectionUtils.isEmpty(root.getChilds())) {
            throw new RuntimeException("step无步骤信息");
        }
        for (StepNode child : root.getChilds()) {
            switch (child.getCodeType()) {
                case TYPE:
                    // 模拟执行execute
                    System.out.println(root.getDesc() + "-" + child.getDesc());
                    break;
                case PACKAGE:
                    _execute(child);
                    break;
            }
        }

    }

}
