package com.shon.step.core.anotation;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
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
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: shiye.ys
 * @date: 2022/5/16
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.shon.step.core.anotation.StepErrorCode")
@AutoService(Processor.class)
public class StepErrorCodeProcessor2 extends AbstractProcessor {

    private Messager messager;
    private JavacTrees javacTrees;
    private TreeMaker treeMaker;
    private Names names;
    private Elements elements;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.javacTrees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment)processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
        this.elements = processingEnv.getElementUtils();
        this.filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Trees trees = Trees.instance(processingEnv);
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(StepErrorCode.class);
        Set<TypeElement> typeElements = ElementFilter.typesIn(elements);
        if (CollectionUtils.isEmpty(typeElements)) {
            return false;
        }
        PrintWriter out = null;
        try {
            FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "step-error-code.properties");
            String name = StandardLocation.CLASS_OUTPUT.getName();
            out = new PrintWriter(new OutputStreamWriter(fileObject.openOutputStream(), StandardCharsets.UTF_8));
            Types TypeUtils = this.processingEnv.getTypeUtils();
            for (TypeElement typeElement : typeElements) {

                // 注解
                StepErrorCode annotation = typeElement.getAnnotation(StepErrorCode.class);
                Element element = TypeUtils.asElement(getTypeMirror(annotation));
                // 所有方法
                List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
                List<ExecutableElement> executableElements = ElementFilter.methodsIn(enclosedElements);
                Set<String> errorCodes = Sets.newHashSet();
                for (ExecutableElement executableElement : executableElements) {
                    // 遍历方法
                    System.out.println("----StepErrorCodeProcessor-----");
                    // 方法体代码正则匹配
                    Pattern pattern = Pattern.compile("(?<code>" + element.getSimpleName()+ "\\.[_a-zA-Z0-9]*)[ );,]");
                    MethodTree methodTree = trees.getTree(executableElement);
                    BlockTree blockTree = methodTree.getBody();
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
                out.println(typeElement.toString() + "=" + StringUtils.join(errorCodes, ","));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (null != out) {
                out.close();
            }
        }

        return true;
    }

    private static TypeMirror getTypeMirror(StepErrorCode annotation) {
        try {
            annotation.errorCodeEnum(); // this should throw
        } catch(MirroredTypeException mte ) {
            return mte.getTypeMirror();
        }
        return null; // can this ever happen ??
    }

}
