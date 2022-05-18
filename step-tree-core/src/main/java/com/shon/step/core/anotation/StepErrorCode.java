package com.shon.step.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: shiye.ys
 * @date: 2022/5/16
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface StepErrorCode {
    Class<?> errorCodeEnum();
}
