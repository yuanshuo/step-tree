package com.shon.step.test.util;

import com.shon.step.core.anotation.StepErrorCode;
import com.shon.step.test.demo.ErrorCodeEnum;

/**
 * @author: shiye.ys
 * @date: 2022/5/18
 */
@StepErrorCode(errorCodeEnum = ErrorCodeEnum.class)
public class CommonUtil {

    public void util() {
        System.out.println(ErrorCodeEnum.TEST_ERROR_CODE4);
    }
}
