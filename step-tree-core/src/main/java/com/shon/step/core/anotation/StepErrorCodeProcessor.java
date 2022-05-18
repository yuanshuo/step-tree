package com.shon.step.core.anotation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import com.google.auto.service.AutoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.testing.compile.JavaFileObjects;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;

/**
 * @author: shiye.ys
 * @date: 2022/5/16
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.shon.step.core.anotation.StepErrorCode")
@AutoService(Processor.class)
public class StepErrorCodeProcessor extends AbstractProcessor {

    private Messager messager;
    private JavacTrees javacTrees;
    private TreeMaker treeMaker;
    private Names names;
    private Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.javacTrees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment)processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
        this.elements = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Trees trees = Trees.instance(processingEnv);
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(StepErrorCode.class);
        Set<TypeElement> typeElements = ElementFilter.typesIn(elements);
        if (CollectionUtils.isEmpty(typeElements)) {
            return false;
        }
        for (TypeElement typeElement : typeElements) {
            // 父类
            TypeMirror superclass = typeElement.getSuperclass();

            // 注解
            StepErrorCode annotation = typeElement.getAnnotation(StepErrorCode.class);
            // 所有方法
            List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
            List<ExecutableElement> executableElements = ElementFilter.methodsIn(enclosedElements);

            for (ExecutableElement executableElement : executableElements) {
                // 遍历方法
                System.out.println("----StepErrorCodeProcessor-----");
                // 方法体代码正则匹配
                Pattern pattern = Pattern.compile("(?<code>"+annotation.errorCodeEnum() + ".*)[ ;,]+");
                MethodTree methodTree = trees.getTree(executableElement);
                BlockTree blockTree = methodTree.getBody();
                Set<String> errorCodes = Sets.newHashSet();
                for (StatementTree statementTree : blockTree.getStatements()) {
                    String s = statementTree.toString();
                    Matcher matcher = pattern.matcher(s);
                    if (matcher.find()) {
                        String code = matcher.group("code");
                        errorCodes.add(code);
                        System.out.println(code);
                    }
                }

            }
            // 生成getErrorCodes方法，在json序列化的时候就可以出来
            JCTree tree = javacTrees.getTree(typeElement);
            tree.accept(new TreeTranslator(){
                @Override
                public void visitClassDef(JCClassDecl jcClassDecl) {
                    jcClassDecl.defs = jcClassDecl.defs.prepend(buildMethod());
                    super.visitClassDef(jcClassDecl);
                }
            });
        }

        return true;
    }
    /*
        有参有返回值
        public String test3(String name){
           return name;
        }
     */
    private JCTree.JCMethodDecl buildMethod() {
        ListBuffer<JCTree.JCStatement> testStatement3 = new ListBuffer<>();
        //testStatement3.append(treeMaker.Return(treeMaker.Ident(names.fromString("name"))));
        treeMaker.NewClass(
            null,
            com.sun.tools.javac.util.List.nil(),
            treeMaker.TypeApply(
                treeMaker.QualIdent((Symbol) elements.getTypeElement("java.util.List")),
                com.sun.tools.javac.util.List.of(treeMaker.QualIdent((Symbol) elements.getTypeElement("java.lang.String")))
            ),
            com.sun.tools.javac.util.List.nil(),
            null
        );
        testStatement3.append(treeMaker.Return(treeMaker.Literal(TypeTag.INT, 11)));
        JCTree.JCBlock testBody3 = treeMaker.Block(0, testStatement3.toList());
        // 生成入参
        JCTree.JCVariableDecl param3 = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER),
            names.fromString("name"),
            treeMaker.Ident(names.fromString("String")), null);
        com.sun.tools.javac.util.List<JCTree.JCVariableDecl> parameters3 = com.sun.tools.javac.util.List.of(param3);

        JCTree.JCMethodDecl test3 = treeMaker.MethodDef(
            treeMaker.Modifiers(Flags.PUBLIC), // 方法限定值
            names.fromString("getNameTest"), // 方法名
            treeMaker.Ident(names.fromString("Integer")), // 返回类型
            com.sun.tools.javac.util.List.nil(),
            com.sun.tools.javac.util.List.nil(), // 入参
            com.sun.tools.javac.util.List.nil(),
            testBody3,
            null
        );
        return test3;
    }
}
