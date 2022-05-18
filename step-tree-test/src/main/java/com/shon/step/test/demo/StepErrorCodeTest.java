package com.shon.step.test.demo;

import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compilation.Status;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import com.shon.step.core.anotation.StepErrorCodeProcessor;

/**
 * @author: shiye.ys
 * @date: 2022/5/16
 */
public class StepErrorCodeTest {

    public static void main(String[] args) {

        // Create a source file to process, or load one from disk.
        JavaFileObject file = JavaFileObjects.forSourceLines("com.shon.step.TestErrorCode",
            "package com.shon.step.test;",
            "",
            "import com.shon.step.core.anotation.StepErrorCode;",
            "import com.shon.step.test.demo.ErrorCodeEnum;",
            "import com.shon.step.test.demo.AbstractTest;",
            "@StepErrorCode(errorCodeEnum=\"ErrorCodeEnum\")",
            "public class TestErrorCode extends AbstractTest {",
            "  ",
            "  public String exec(){",
            "    Integer a = 1; return ErrorCodeEnum.TEST_ERROR_CODE ; ",
            "  }",
            "}");
        //URL resource = Test.class.getClassLoader().getResource("/test/java/com/shon/step/test/StepErrorCodeTest.java");
        //JavaFileObject javaFile = JavaFileObjects.forResource(resource);
        StepErrorCodeProcessor processor = new StepErrorCodeProcessor();
        Compiler compiler = Compiler.javac().withProcessors(processor);
        Compilation compile = compiler.compile(file);
        if (compile.status().equals(Status.FAILURE)) {
            for (Diagnostic<? extends JavaFileObject> error : compile.errors()) {
                System.out.println(error.getMessage(Locale.CHINA));
            }
        }
    }
}
