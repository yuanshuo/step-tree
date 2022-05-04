package com.shon.step.core;

import java.lang.annotation.ElementType;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 步骤执行节点
 */
@Getter
@Setter
public class StepNode {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 步骤下级子步骤
     */
    private List<StepNode> childs;

    /**
     * 代码类型
     * @see ElementType
     */
    private ElementType codeType;

    /**
     * 代码包路径
     */
    private String codePkgPath;

    /**
     * 代码对应类
     */
    private Class<?> codeClazz;

}
